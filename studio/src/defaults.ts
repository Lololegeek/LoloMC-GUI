export const defaultXml = `<screen id="server-screen" class="app-shell">
  <panel id="sidebar" class="sidebar">
    <panel class="brand-row">
      <badge class="brand-mark">L</badge>
      <panel>
        <label class="eyebrow">LOLOMC GUI</label>
        <label class="brand-title">Servers</label>
      </panel>
    </panel>
    <label class="nav-label">NAVIGATION</label>
    <button class="nav-item active">Public servers</button>
    <button class="nav-item">My favorites</button>
    <button class="nav-item">Direct connection</button>
    <spacer flex="1" />
    <panel class="profile">
      <avatar>LV</avatar>
      <panel><label>Loevan</label><label class="muted">Ready to play</label></panel>
    </panel>
  </panel>
  <panel id="content" class="content">
    <panel class="topbar">
      <panel><label class="eyebrow">MULTIPLAYER</label><label class="heading">Choose a world</label></panel>
      <input id="search" placeholder="Search servers..." />
    </panel>
    <panel class="hero">
      <badge class="status">FEATURED</badge>
      <label class="hero-title">LoloCraft Origins</label>
      <label class="hero-copy">Survival, economy, and community adventures.</label>
      <panel class="hero-actions">
        <button id="join" class="primary" on-click="joinServer">Join server</button>
        <button class="ghost">View details</button>
      </panel>
    </panel>
    <panel class="section-head"><label class="section-title">Online servers</label><label class="muted">3 available</label></panel>
    <panel class="server-grid">
      <panel class="server-card"><badge>AN</badge><label class="card-title">Anarchy</label><label class="muted">142 joueurs</label></panel>
      <panel class="server-card"><badge>CR</badge><label class="card-title">Creative</label><label class="muted">38 players</label></panel>
      <panel class="server-card"><badge>SK</badge><label class="card-title">Skyblock</label><label class="muted">89 joueurs</label></panel>
    </panel>
  </panel>
</screen>`;

export const defaultCss = `:root {
  --ink: #f2eee6;
  --muted: #858991;
  --panel: #15181d;
  --line: #2a2e35;
  --copper: #e9a23b;
}

screen { flex-direction: row; background: #0d0f12; color: var(--ink); font-size: 11; }
.sidebar { width: 224; padding: 18; gap: 8; background: #111419; border-width: 1; border-color: #242830; }
.brand-row { height: 48; flex-direction: row; gap: 10; }
.brand-mark { width: 38; height: 38; background: var(--copper); color: #17120b; border-radius: 8; font-size: 18; }
.eyebrow { height: 16; color: var(--copper); font-size: 9; }
.brand-title { height: 22; font-size: 16; }
.nav-label { height: 28; color: #61656d; font-size: 9; }
.nav-item { height: 36; padding: 10; color: #989ca5; border-radius: 6; }
.nav-item:hover, .nav-item.active { background: #20242b; color: #ffffff; }
spacer { flex: 1; }
.profile { height: 50; padding: 7; gap: 10; flex-direction: row; background: #191c22; border-radius: 7; }
avatar, badge { width: 34; height: 34; background: #2a2e36; border-radius: 6; color: var(--copper); }
.muted { height: 16; color: var(--muted); font-size: 9; }
.content { flex: 1; padding: 24; gap: 16; }
.topbar { height: 54; flex-direction: row; gap: 18; }
.topbar panel { flex: 1; }
.heading { height: 30; font-size: 22; }
input { width: 230; height: 36; padding: 10; background: #16191e; border-width: 1; border-color: var(--line); border-radius: 6; color: #b8bbc2; }
.hero { height: 174; padding: 22; gap: 8; background: #1a1b1d; border-width: 1; border-color: #3b3428; border-radius: 10; }
.status { width: 88; height: 20; background: #332817; border-radius: 4; color: var(--copper); font-size: 8; }
.hero-title { height: 30; font-size: 24; }
.hero-copy { height: 18; color: #a4a6ab; }
.hero-actions { height: 38; flex-direction: row; gap: 8; }
button { border-radius: 6; color: #d7d8dc; }
button.primary { width: 120; background: var(--copper); color: #1d160c; }
button.primary:hover { background: #ffc061; }
button.ghost { width: 130; background: #272a2f; }
.section-head { height: 28; flex-direction: row; }
.section-title { flex: 1; font-size: 15; }
.server-grid { flex: 1; flex-direction: row; gap: 12; }
.server-card { flex: 1; height: 110; padding: 14; gap: 6; background: var(--panel); border-width: 1; border-color: var(--line); border-radius: 8; }
.server-card:hover { border-color: #66502f; background: #1b1e23; }
.card-title { height: 20; font-size: 14; }`;
