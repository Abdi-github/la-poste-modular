/* ══════════════════════════════════════════════════════
   La Poste Modular — Application JavaScript
   ══════════════════════════════════════════════════════ */
document.addEventListener('DOMContentLoaded', () => {
  // ── Sidebar Toggle ──────────────────────────────────
  const toggle = document.getElementById('sidebarToggle');
  const sidebar = document.getElementById('sidebar');
  const backdrop = document.getElementById('sidebarBackdrop');

  if (toggle && sidebar) {
    toggle.addEventListener('click', () => {
      sidebar.classList.toggle('show');
      backdrop?.classList.toggle('show');
    });
    backdrop?.addEventListener('click', () => {
      sidebar.classList.remove('show');
      backdrop.classList.remove('show');
    });
  }

  // ── Language Switch (preserve current path) ─────────
  document.querySelectorAll('.lang-switch').forEach(link => {
    link.addEventListener('click', (e) => {
      e.preventDefault();
      const url = new URL(window.location.href);
      const lang = new URL(link.href).searchParams.get('lang');
      url.searchParams.set('lang', lang);
      window.location.href = url.toString();
    });
  });
});

// ── Sort Toggle (preserves all other URL params) ──────
function toggleSort(el) {
  const field = el.dataset.field;
  const url = new URL(window.location.href);
  const currentSort = url.searchParams.get('sort');
  let newDir = 'ASC';
  if (currentSort && currentSort.startsWith(field + ',')) {
    newDir = currentSort.toLowerCase().endsWith(',asc') ? 'DESC' : 'ASC';
  }
  url.searchParams.set('sort', field + ',' + newDir);
  url.searchParams.set('page', '0');
  window.location.href = url.toString();
}

// ── Pagination (preserves all other URL params) ───────
function goToPage(el) {
  const page = el.dataset.page;
  if (page === undefined || page === null) return;
  const url = new URL(window.location.href);
  url.searchParams.set('page', page);
  window.location.href = url.toString();
}
