import { useMemo, useRef, useState } from 'react';
import JSZip from 'jszip';
import {
  Box, Braces, ChevronDown, ChevronRight, Code2, FileCode2, Image,
  Layers3, Monitor, MousePointer2, PackageOpen, PanelTop, Plus, Redo2,
  Search, Smartphone, Trash2, Type, Undo2, Upload, WandSparkles,
} from 'lucide-react';
import { defaultCss, defaultXml } from './defaults';
import { addNode, findNode, parseUi, removeNode, type UiElement, updateNode } from './model';
import { Preview } from './Preview';

type Project = { xml: string; css: string };
type Tab = 'design' | 'xml' | 'css';
const palette = [
  { tag: 'panel', label: 'Panneau', icon: PanelTop },
  { tag: 'label', label: 'Texte', icon: Type },
  { tag: 'button', label: 'Bouton', icon: MousePointer2 },
  { tag: 'image', label: 'Image', icon: Image },
  { tag: 'input', label: 'Champ', icon: Box },
  { tag: 'badge', label: 'Badge', icon: WandSparkles },
];
const viewports = { compact: [854, 480], standard: [960, 540], wide: [1280, 720] } as const;

export function App() {
  const [project, setProjectRaw] = useState<Project>({ xml: defaultXml, css: defaultCss });
  const [selectedId, setSelectedId] = useState<string | null>('join');
  const [tab, setTab] = useState<Tab>('design');
  const [viewport, setViewport] = useState<keyof typeof viewports>('standard');
  const [zoom, setZoom] = useState(0.82);
  const [query, setQuery] = useState('');
  const [status, setStatus] = useState('Projet prêt');
  const past = useRef<Project[]>([]);
  const future = useRef<Project[]>([]);
  const fileInput = useRef<HTMLInputElement>(null);

  const parsed = useMemo(() => {
    try { return { root: parseUi(project.xml), error: null }; }
    catch (error) { return { root: null, error: error instanceof Error ? error.message : 'XML invalide' }; }
  }, [project.xml]);
  const selected = parsed.root ? findNode(parsed.root, selectedId) : null;

  const commit = (next: Project, message = 'Modification enregistrée') => {
    if (next.xml === project.xml && next.css === project.css) return;
    past.current.push(project);
    if (past.current.length > 60) past.current.shift();
    future.current = [];
    setProjectRaw(next);
    setStatus(message);
  };
  const undo = () => {
    const previous = past.current.pop();
    if (!previous) return;
    future.current.push(project); setProjectRaw(previous); setStatus('Modification annulée');
  };
  const redo = () => {
    const next = future.current.pop();
    if (!next) return;
    past.current.push(project); setProjectRaw(next); setStatus('Modification rétablie');
  };
  const add = (tag: string) => {
    try {
      const parent = selected && ['screen', 'panel'].includes(selected.tag) ? selectedId : null;
      const result = addNode(project.xml, parent, tag);
      commit({ ...project, xml: result.xml }, `${tag} ajouté`); setSelectedId(result.id);
    } catch (error) { setStatus(error instanceof Error ? error.message : 'Ajout impossible'); }
  };
  const update = (name: string, value: string) => {
    if (!selectedId) return;
    try {
      commit({ ...project, xml: updateNode(project.xml, selectedId, name, value) });
      if (name === 'id') setSelectedId(value || null);
    }
    catch (error) { setStatus(error instanceof Error ? error.message : 'Modification impossible'); }
  };
  const updateStyle = (name: string, value: string) => {
    if (!selected) return;
    const declarations = parseInlineStyle(selected.attributes.style || '');
    if (value) declarations[name] = value; else delete declarations[name];
    update('style', Object.entries(declarations).map(([key, val]) => `${key}: ${val}`).join('; '));
  };
  const moveNode = (id: string, left: number, top: number) => {
    const node = parsed.root ? findNode(parsed.root, id) : null;
    if (!node) return;
    const declarations = parseInlineStyle(node.attributes.style || '');
    declarations.position = 'absolute';
    declarations.left = `${Math.max(0, left)}px`;
    declarations.top = `${Math.max(0, top)}px`;
    commit({ ...project, xml: updateNode(project.xml, id, 'style', Object.entries(declarations).map(([key, val]) => `${key}: ${val}`).join('; ')) }, 'Élément déplacé');
  };
  const remove = () => {
    if (!selectedId || selectedId === parsed.root?.attributes.id) return;
    commit({ ...project, xml: removeNode(project.xml, selectedId) }, 'Élément supprimé'); setSelectedId(null);
  };
  const exportProject = async () => {
    const zip = new JSZip();
    zip.file('screen.xml', project.xml); zip.file('screen.css', project.css);
    zip.file('README.txt', 'Copiez ces fichiers dans src/main/resources/assets/<modid>/ui/.\nChargez-les avec LoloGui.load(xmlStream, cssText).');
    const blob = await zip.generateAsync({ type: 'blob' });
    const link = document.createElement('a'); link.href = URL.createObjectURL(blob); link.download = 'lolomc-gui-screen.zip'; link.click(); URL.revokeObjectURL(link.href);
    setStatus('Projet exporté');
  };
  const importFiles = async (files: FileList | null) => {
    if (!files) return;
    let next = project;
    for (const file of Array.from(files)) {
      const content = await file.text();
      if (file.name.endsWith('.xml')) next = { ...next, xml: content };
      if (file.name.endsWith('.css')) next = { ...next, css: content };
    }
    commit(next, 'Fichiers importés');
  };
  const [canvasWidth, canvasHeight] = viewports[viewport];

  return (
    <main className="studio-shell">
      <header className="app-header">
        <div className="wordmark"><span className="logo-cube"><Layers3 size={18} /></span><span><b>LoloMC GUI</b><small>STUDIO • 0.1</small></span></div>
        <div className="project-title"><span className="live-dot" /> server-selector <small>/ screen.xml</small></div>
        <div className="toolbar">
          <button title="Annuler" onClick={undo} disabled={!past.current.length}><Undo2 size={16} /></button>
          <button title="Rétablir" onClick={redo} disabled={!future.current.length}><Redo2 size={16} /></button>
          <span className="toolbar-separator" />
          <button onClick={() => fileInput.current?.click()}><Upload size={15} /> Importer</button>
          <input ref={fileInput} hidden multiple type="file" accept=".xml,.css" onChange={(event) => void importFiles(event.target.files)} />
          <button className="export-button" onClick={() => void exportProject()}><PackageOpen size={15} /> Exporter</button>
        </div>
      </header>

      <section className="workspace">
        <aside className="left-rail">
          <PanelTitle icon={<Plus size={14} />} label="COMPOSANTS" />
          <div className="component-search"><Search size={14} /><input value={query} onChange={(e) => setQuery(e.target.value)} placeholder="Rechercher" /></div>
          <div className="palette">
            {palette.filter((item) => item.label.toLowerCase().includes(query.toLowerCase())).map(({ tag, label, icon: Icon }) => (
              <button key={tag} onClick={() => add(tag)}><Icon size={17} /><span>{label}</span><Plus size={12} /></button>
            ))}
          </div>
          <PanelTitle icon={<Braces size={14} />} label="ARBRE DU DOCUMENT" />
          <div className="tree">{parsed.root ? <TreeNode node={parsed.root} selectedId={selectedId} onSelect={setSelectedId} /> : <p className="error-copy">{parsed.error}</p>}</div>
          <div className="library-hint"><WandSparkles size={16} /><div><b>Prêt pour Minecraft</b><span>XML + CSS, aucun loader imposé.</span></div></div>
        </aside>

        <section className="stage-column">
          <div className="stage-toolbar">
            <div className="mode-tabs">
              <button className={tab === 'design' ? 'active' : ''} onClick={() => setTab('design')}><Monitor size={14} /> Design</button>
              <button className={tab === 'xml' ? 'active' : ''} onClick={() => setTab('xml')}><FileCode2 size={14} /> XML</button>
              <button className={tab === 'css' ? 'active' : ''} onClick={() => setTab('css')}><Code2 size={14} /> CSS</button>
            </div>
            {tab === 'design' && <div className="viewport-tools">
              <Smartphone size={14} />
              <select value={viewport} onChange={(e) => setViewport(e.target.value as keyof typeof viewports)}>
                <option value="compact">854 × 480</option><option value="standard">960 × 540</option><option value="wide">1280 × 720</option>
              </select>
              <input aria-label="Zoom" type="range" min="0.45" max="1" step="0.05" value={zoom} onChange={(e) => setZoom(Number(e.target.value))} />
              <span>{Math.round(zoom * 100)}%</span>
            </div>}
          </div>
          {tab === 'design' ? (
            <div className="canvas-wrap">
              <div className="canvas-label"><span>{canvasWidth} × {canvasHeight}</span><span>APERÇU INTERACTIF</span></div>
              <div className="game-canvas" style={{ width: canvasWidth, height: canvasHeight, transform: `scale(${zoom})` }}>
                {parsed.root ? <Preview root={parsed.root} css={project.css} selectedId={selectedId} zoom={zoom} onSelect={setSelectedId} onMove={moveNode} /> : <div className="xml-error"><Code2 size={28} /><b>Le XML ne peut pas être affiché</b><span>{parsed.error}</span></div>}
              </div>
            </div>
          ) : (
            <div className="code-workspace">
              <div className="code-filebar"><span>{tab === 'xml' ? 'screen.xml' : 'screen.css'}</span><span>{tab.toUpperCase()} • UTF-8</span></div>
              <textarea spellCheck={false} value={tab === 'xml' ? project.xml : project.css} onChange={(e) => commit(tab === 'xml' ? { ...project, xml: e.target.value } : { ...project, css: e.target.value }, `${tab.toUpperCase()} modifié`)} />
            </div>
          )}
          <footer className="statusbar"><span><span className="status-check">✓</span> {status}</span><span>{parsed.error ? `Erreur : ${parsed.error}` : `${countNodes(parsed.root)} éléments • aucune dépendance loader`}</span></footer>
        </section>

        <aside className="inspector">
          <PanelTitle icon={<MousePointer2 size={14} />} label="INSPECTEUR" />
          {selected ? <>
            <div className="selection-card"><span className="selection-icon">{selected.tag.slice(0, 2).toUpperCase()}</span><div><b>{selected.attributes.id || selected.tag}</b><span>&lt;{selected.tag}&gt;</span></div><button onClick={remove} title="Supprimer"><Trash2 size={15} /></button></div>
            <InspectorGroup title="IDENTITÉ">
              <Field label="ID" value={selected.attributes.id || ''} onChange={(value) => update('id', value)} />
              <Field label="Classes" value={selected.attributes.class || ''} onChange={(value) => update('class', value)} />
              {selected.tag !== 'panel' && selected.tag !== 'screen' && <Field label="Texte" value={selected.text} onChange={(value) => update('$text', value)} />}
              {selected.tag === 'button' && <Field label="Action" value={selected.attributes['on-click'] || ''} onChange={(value) => update('on-click', value)} />}
            </InspectorGroup>
            <InspectorGroup title="DIMENSIONS">
              <div className="drag-hint"><MousePointer2 size={13} /><span>Glissez l’élément dans l’aperçu pour le positionner.</span></div>
              <div className="field-grid"><Field label="Position X" value={inlineValue(selected, 'left')} placeholder="auto" onChange={(v) => updateStyle('left', v)} /><Field label="Position Y" value={inlineValue(selected, 'top')} placeholder="auto" onChange={(v) => updateStyle('top', v)} /></div>
              <div className="field-grid"><Field label="Largeur" value={inlineValue(selected, 'width')} placeholder="auto" onChange={(v) => updateStyle('width', v)} /><Field label="Hauteur" value={inlineValue(selected, 'height')} placeholder="auto" onChange={(v) => updateStyle('height', v)} /></div>
              <div className="field-grid"><Field label="Espacement" value={inlineValue(selected, 'gap')} placeholder="0" onChange={(v) => updateStyle('gap', v)} /><Field label="Marge int." value={inlineValue(selected, 'padding')} placeholder="0" onChange={(v) => updateStyle('padding', v)} /></div>
            </InspectorGroup>
            <InspectorGroup title="APPARENCE">
              <Field label="Fond" value={inlineValue(selected, 'background')} placeholder="CSS / var(...)" onChange={(v) => updateStyle('background', v)} />
              <Field label="Couleur" value={inlineValue(selected, 'color')} placeholder="héritée" onChange={(v) => updateStyle('color', v)} />
              <Field label="Rayon" value={inlineValue(selected, 'border-radius')} placeholder="0" onChange={(v) => updateStyle('border-radius', v)} />
            </InspectorGroup>
          </> : <div className="empty-inspector"><MousePointer2 size={24} /><b>Sélectionnez un élément</b><span>Cliquez dans l’aperçu ou dans l’arbre.</span></div>}
        </aside>
      </section>
    </main>
  );
}

function PanelTitle({ icon, label }: { icon: React.ReactNode; label: string }) { return <div className="panel-title">{icon}<span>{label}</span></div>; }
function InspectorGroup({ title, children }: { title: string; children: React.ReactNode }) { return <section className="inspector-group"><h3>{title}<ChevronDown size={13} /></h3>{children}</section>; }
function Field({ label, value, onChange, placeholder }: { label: string; value: string; onChange: (value: string) => void; placeholder?: string }) { return <label className="property-field"><span>{label}</span><input value={value} placeholder={placeholder} onChange={(e) => onChange(e.target.value)} /></label>; }

function TreeNode({ node, selectedId, onSelect, depth = 0 }: { node: UiElement; selectedId: string | null; onSelect: (id: string | null) => void; depth?: number }) {
  const [open, setOpen] = useState(true);
  const id = node.attributes.id || null;
  return <div>
    <div className={`tree-row ${id && id === selectedId ? 'selected' : ''}`} style={{ paddingLeft: 7 + depth * 13 }} onClick={() => onSelect(id)}>
      <button onClick={(e) => { e.stopPropagation(); setOpen(!open); }}>{node.children.length ? (open ? <ChevronDown size={12} /> : <ChevronRight size={12} />) : <span />}</button>
      <span className="tag-icon">{node.tag[0].toUpperCase()}</span><span>{id || node.tag}</span><small>{node.tag}</small>
    </div>
    {open && node.children.map((child, index) => <TreeNode key={`${child.attributes.id || child.tag}-${index}`} node={child} selectedId={selectedId} onSelect={onSelect} depth={depth + 1} />)}
  </div>;
}

function inlineValue(node: UiElement, property: string): string {
  return parseInlineStyle(node.attributes.style || '')[property] || '';
}
function parseInlineStyle(style: string): Record<string, string> {
  const result: Record<string, string> = {};
  for (const declaration of style.split(';')) {
    const colon = declaration.indexOf(':');
    if (colon <= 0) continue;
    const key = declaration.slice(0, colon).trim();
    const value = declaration.slice(colon + 1).trim();
    if (key && value) result[key] = value;
  }
  return result;
}
function countNodes(root: UiElement | null): number { return root ? 1 + root.children.reduce((sum, child) => sum + countNodes(child), 0) : 0; }
