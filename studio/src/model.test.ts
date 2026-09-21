import { describe, expect, it } from 'vitest';
import { addNode, findNode, parseUi, removeNode, updateNode } from './model';
import { normalizeCss } from './Preview';

describe('LoloMC GUI document model', () => {
  it('parses and edits a document', () => {
    const source = '<screen id="root"><panel id="body"><label id="title">Hello</label></panel></screen>';
    const parsed = parseUi(source);
    expect(findNode(parsed, 'title')?.text).toBe('Hello');
    const changed = updateNode(source, 'title', '$text', 'Bonjour');
    expect(findNode(parseUi(changed), 'title')?.text).toBe('Bonjour');
    const added = addNode(changed, 'body', 'button');
    expect(findNode(parseUi(added.xml), added.id)?.tag).toBe('button');
    expect(findNode(parseUi(removeNode(added.xml, added.id)), added.id)).toBeNull();
  });

  it('rejects malformed XML', () => {
    expect(() => parseUi('<screen><panel></screen>')).toThrow();
  });

  it('scopes preview CSS away from the Studio', () => {
    const css = normalizeCss(':root{--accent:#fff} button, panel:hover{height:30}');
    expect(css).toContain('.preview-root {');
    expect(css).toContain('.preview-root button');
    expect(css).toContain('.preview-root lolo-panel:hover');
    expect(css).not.toMatch(/(^|})\s*button\s*[,{]/);
  });
});
