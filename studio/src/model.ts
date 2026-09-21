export type UiElement = {
  tag: string;
  attributes: Record<string, string>;
  text: string;
  children: UiElement[];
};

const parserError = 'parsererror';

export function parseUi(xml: string): UiElement {
  const document = new DOMParser().parseFromString(xml, 'application/xml');
  const error = document.querySelector(parserError);
  if (error) throw new Error(error.textContent?.split('\n')[0] || 'XML invalide');
  return fromElement(document.documentElement);
}

function fromElement(element: Element): UiElement {
  const attributes: Record<string, string> = {};
  for (const attribute of Array.from(element.attributes)) attributes[attribute.name] = attribute.value;
  const text = Array.from(element.childNodes)
    .filter((node) => node.nodeType === Node.TEXT_NODE)
    .map((node) => node.textContent || '')
    .join('')
    .trim();
  return {
    tag: element.tagName.toLowerCase(),
    attributes,
    text,
    children: Array.from(element.children).map(fromElement),
  };
}

function mutate(xml: string, mutation: (document: XMLDocument) => void): string {
  const document = new DOMParser().parseFromString(xml, 'application/xml');
  if (document.querySelector(parserError)) throw new Error('Impossible de modifier un XML invalide');
  mutation(document);
  return formatXml(new XMLSerializer().serializeToString(document));
}

export function updateNode(xml: string, id: string, name: string, value: string): string {
  return mutate(xml, (document) => {
    const element = findElement(document, id);
    if (!element) return;
    if (name === '$text') element.textContent = value;
    else if (value) element.setAttribute(name, value);
    else element.removeAttribute(name);
  });
}

export function addNode(xml: string, parentId: string | null, tag: string): { xml: string; id: string } {
  const id = `${tag}-${Math.random().toString(36).slice(2, 7)}`;
  const updated = mutate(xml, (document) => {
    const parent = parentId ? findElement(document, parentId) : document.documentElement;
    if (!parent) return;
    const child = document.createElement(tag);
    child.setAttribute('id', id);
    if (tag === 'label') child.textContent = 'Nouveau texte';
    if (tag === 'button') { child.textContent = 'Bouton'; child.setAttribute('on-click', 'action'); }
    if (tag === 'image') child.setAttribute('src', 'minecraft:textures/item/diamond.png');
    if (tag === 'progress') { child.setAttribute('value', '0.65'); child.setAttribute('max', '1'); }
    parent.appendChild(child);
  });
  return { xml: updated, id };
}

export function removeNode(xml: string, id: string): string {
  return mutate(xml, (document) => findElement(document, id)?.remove());
}

function findElement(document: XMLDocument, id: string): Element | null {
  return Array.from(document.getElementsByTagName('*')).find((element) => element.getAttribute('id') === id) || null;
}

export function findNode(root: UiElement, id: string | null): UiElement | null {
  if (!id) return null;
  if (root.attributes.id === id) return root;
  for (const child of root.children) {
    const found = findNode(child, id);
    if (found) return found;
  }
  return null;
}

export function formatXml(xml: string): string {
  const compact = xml.replace(/>\s+</g, '><').trim();
  let depth = 0;
  return compact
    .replace(/(<[^>]+>)/g, '$1\n')
    .trim()
    .split('\n')
    .map((line) => {
      if (/^<\//.test(line)) depth = Math.max(0, depth - 1);
      const result = `${'  '.repeat(depth)}${line}`;
      if (/^<[^!?/][^>]*>$/.test(line) && !/\/\s*>$/.test(line) && !/<\/[^>]+>$/.test(line)) depth++;
      return result;
    })
    .join('\n');
}
