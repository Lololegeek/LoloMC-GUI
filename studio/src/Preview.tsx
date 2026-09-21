import { createElement, type MouseEvent } from 'react';
import type { UiElement } from './model';

type Props = { root: UiElement; css: string; selectedId: string | null; onSelect: (id: string | null) => void };

const unitProperties = new Set(['width', 'height', 'left', 'top', 'right', 'bottom', 'padding', 'gap', 'font-size', 'border-radius', 'border-width']);

export function normalizeCss(css: string): string {
  const customElements = css.replace(/([^{}]+)\{/g, (block, selector: string) => {
    const rewritten = selector.split(',').map((part) => {
      const normalized = part.trim().replace(/\b(screen|panel|spacer|label|badge|avatar|image)\b/g, 'lolo-$1');
      return normalized === ':root' ? '.preview-root' : `.preview-root ${normalized}`;
    }).join(', ');
    return `${rewritten} {`;
  });
  return customElements.replace(/([\w-]+)\s*:\s*(-?\d+(?:\.\d+)?)\s*(?=[;}])/g, (match, property: string, value: string) =>
    unitProperties.has(property) && property !== 'flex' ? `${property}: ${value}px` : match,
  );
}

function NodeView({ node, selectedId, onSelect }: { node: UiElement; selectedId: string | null; onSelect: Props['onSelect'] }) {
  const id = node.attributes.id;
  const click = (event: MouseEvent) => { event.stopPropagation(); onSelect(id || null); };
  const attributes = { ...node.attributes };
  const className = attributes.class;
  delete attributes.class;
  delete attributes['on-click'];
  delete attributes.src;
  const common = {
    ...attributes,
    className,
    'data-lolo-node': id || node.tag,
    'data-selected': id && id === selectedId ? 'true' : undefined,
    onClick: click,
  };
  const children = <>{node.text}{node.children.map((child, index) => <NodeView key={`${child.attributes.id || child.tag}-${index}`} node={child} selectedId={selectedId} onSelect={onSelect} />)}</>;
  if (node.tag === 'input') return <input {...common} readOnly value={node.attributes.value || ''} />;
  const tag = node.tag === 'button' ? 'button' : `lolo-${node.tag}`;
  return createElement(tag, common, children);
}

export function Preview({ root, css, selectedId, onSelect }: Props) {
  return (
    <div className="preview-root" onClick={() => onSelect(null)}>
      <style>{`${normalizeCss(css)}\n.preview-root [data-selected="true"] { outline: 1px solid #ffb44c !important; outline-offset: 2px; }`}</style>
      <NodeView node={root} selectedId={selectedId} onSelect={onSelect} />
    </div>
  );
}
