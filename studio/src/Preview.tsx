import { createElement, useRef, useState, type CSSProperties, type MouseEvent, type PointerEvent } from 'react';
import type { UiElement } from './model';

type Props = { root: UiElement; css: string; selectedId: string | null; zoom: number; onSelect: (id: string | null) => void; onMove: (id: string, left: number, top: number) => void };

const unitProperties = new Set(['width', 'height', 'left', 'top', 'right', 'bottom', 'padding', 'margin', 'gap', 'font-size', 'line-height', 'letter-spacing', 'border-radius', 'border-width', 'min-width', 'max-width', 'min-height', 'max-height']);

export function normalizeCss(css: string): string {
  const customElements = css.replace(/([^{}]+)\{/g, (block, selector: string) => {
    const rewritten = selector.split(',').map((part) => {
      const normalized = part.trim().replace(/\b(screen|panel|spacer|label|badge|avatar|image|progress)\b/g, 'lolo-$1');
      return normalized === ':root' ? '.preview-root' : `.preview-root ${normalized}`;
    }).join(', ');
    return `${rewritten} {`;
  });
  return customElements.replace(/([\w-]+)\s*:\s*([^;}]+)(?=[;}])/g, (match, property: string, value: string) => {
    if (!unitProperties.has(property)) return match;
    const withUnits = value.replace(/-?\d+(?:\.\d+)?(?![%\w.-])/g, (number: string) => `${number}px`);
    return `${property}: ${withUnits}`;
  });
}

function inlineStyle(styleText: string): CSSProperties {
  const result: Record<string, string> = {};
  for (const declaration of styleText.split(';')) {
    const colon = declaration.indexOf(':');
    if (colon <= 0) continue;
    const key = declaration.slice(0, colon).trim();
    let value = declaration.slice(colon + 1).trim();
    if (!key || !value) continue;
    if (unitProperties.has(key)) value = value.replace(/-?\d+(?:\.\d+)?(?![%\w.-])/g, (number) => `${number}px`);
    const reactKey = key.startsWith('--') ? key : key.replace(/-([a-z])/g, (_, letter: string) => letter.toUpperCase());
    result[reactKey] = value;
  }
  return result as CSSProperties;
}

function NodeView({ node, selectedId, zoom, onSelect, onMove }: { node: UiElement; selectedId: string | null; zoom: number; onSelect: Props['onSelect']; onMove: Props['onMove'] }) {
  const id = node.attributes.id;
  const [dragOffset, setDragOffset] = useState({ x: 0, y: 0 });
  const drag = useRef<{ startX: number; startY: number; left: number; top: number; moved: boolean } | null>(null);
  const click = (event: MouseEvent) => { event.stopPropagation(); onSelect(id || null); };
  const pointerDown = (event: PointerEvent<HTMLElement>) => {
    if (!id || event.button !== 0) return;
    event.stopPropagation();
    const element = event.currentTarget;
    const parent = element.parentElement?.getBoundingClientRect();
    const rect = element.getBoundingClientRect();
    if (!parent) return;
    element.setPointerCapture(event.pointerId);
    drag.current = { startX: event.clientX, startY: event.clientY, left: (rect.left - parent.left) / zoom, top: (rect.top - parent.top) / zoom, moved: false };
    setDragOffset({ x: 0, y: 0 });
    onSelect(id);
  };
  const pointerMove = (event: PointerEvent<HTMLElement>) => {
    const state = drag.current;
    if (!state) return;
    const x = (event.clientX - state.startX) / zoom;
    const y = (event.clientY - state.startY) / zoom;
    if (Math.abs(x) > 2 || Math.abs(y) > 2) state.moved = true;
    setDragOffset({ x, y });
  };
  const pointerUp = (event: PointerEvent<HTMLElement>) => {
    const state = drag.current;
    if (!state) return;
    if (state.moved && id) onMove(id, Math.round(state.left + dragOffset.x), Math.round(state.top + dragOffset.y));
    drag.current = null;
    setDragOffset({ x: 0, y: 0 });
    if (event.currentTarget.hasPointerCapture(event.pointerId)) event.currentTarget.releasePointerCapture(event.pointerId);
  };
  const attributes = { ...node.attributes };
  const className = attributes.class;
  delete attributes.class;
  delete attributes['on-click'];
  delete attributes.src;
  delete attributes.style;
  const resolvedStyle = inlineStyle(node.attributes.style || '');
  if (node.tag === 'progress') {
    const value = Number(node.attributes.value || 0);
    const max = Number(node.attributes.max || 1);
    (resolvedStyle as Record<string, string>)['--lolo-progress'] = `${Math.max(0, Math.min(100, max > 0 ? value / max * 100 : 0))}%`;
  }
  const common = {
    ...attributes,
    className,
    style: { ...resolvedStyle, ...(drag.current ? { transform: `translate(${dragOffset.x}px, ${dragOffset.y}px)` } : {}) },
    'data-lolo-node': id || node.tag,
    'data-selected': id && id === selectedId ? 'true' : undefined,
    'data-dragging': drag.current?.moved ? 'true' : undefined,
    onClick: click,
    onPointerDown: pointerDown,
    onPointerMove: pointerMove,
    onPointerUp: pointerUp,
  };
  const children = <>{node.text}{node.children.map((child, index) => <NodeView key={`${child.attributes.id || child.tag}-${index}`} node={child} selectedId={selectedId} zoom={zoom} onSelect={onSelect} onMove={onMove} />)}</>;
  if (node.tag === 'input') return <input {...common} readOnly value={node.attributes.value || ''} />;
  const tag = node.tag === 'button' ? 'button' : `lolo-${node.tag}`;
  return createElement(tag, common, children);
}

export function Preview({ root, css, selectedId, zoom, onSelect, onMove }: Props) {
  return (
    <div className="preview-root" onClick={() => onSelect(null)}>
      <style>{`${normalizeCss(css)}\n.preview-root [data-selected="true"] { outline: 1px solid #ffb44c !important; outline-offset: 2px; }`}</style>
      <NodeView node={root} selectedId={selectedId} zoom={zoom} onSelect={onSelect} onMove={onMove} />
    </div>
  );
}
