/* ==========================================================================
   Cimbyte UI — vanilla behaviors. No dependencies, no framework.
   Drop in <script src="cimbyte.js"></script>. Driven by data-cb-* attributes.
   Exposes window.Cimbyte for imperative use (toasts, open/close).
   ========================================================================== */
(function () {
  "use strict";

  function $(sel, root) { return (root || document).querySelector(sel); }
  function $all(sel, root) { return Array.prototype.slice.call((root || document).querySelectorAll(sel)); }

  /* ----- Dropdown: [data-cb-toggle="dropdown"] toggles the next .cb-menu ----- */
  function closeAllMenus(except) {
    $all(".cb-menu.is-open").forEach(function (m) { if (m !== except) m.classList.remove("is-open"); });
  }
  document.addEventListener("click", function (e) {
    var toggle = e.target.closest('[data-cb-toggle="dropdown"]');
    if (toggle) {
      e.preventDefault();
      var menu = toggle.parentElement.querySelector(".cb-menu");
      if (menu) {
        var willOpen = !menu.classList.contains("is-open");
        closeAllMenus(menu);
        menu.classList.toggle("is-open", willOpen);
      }
      return;
    }
    if (!e.target.closest(".cb-menu")) closeAllMenus();
  });

  /* ----- Modal / drawer: data-cb-toggle="modal" data-cb-target="#id" ----- */
  function openOverlay(id) {
    var el = document.getElementById(id);
    if (!el) return;
    el.classList.add("is-open");
    var scrim = el.previousElementSibling;
    if (scrim && scrim.classList.contains("cb-scrim")) scrim.classList.add("is-open");
  }
  function closeOverlay(el) {
    if (!el) return;
    el.classList.remove("is-open");
    var scrim = el.previousElementSibling;
    if (scrim && scrim.classList.contains("cb-scrim")) scrim.classList.remove("is-open");
  }
  document.addEventListener("click", function (e) {
    var opener = e.target.closest('[data-cb-toggle="modal"],[data-cb-toggle="drawer"]');
    if (opener) {
      e.preventDefault();
      var target = opener.getAttribute("data-cb-target");
      if (target) openOverlay(target.replace(/^#/, ""));
      return;
    }
    var dismiss = e.target.closest('[data-cb-dismiss="modal"],[data-cb-dismiss="drawer"]');
    if (dismiss) {
      e.preventDefault();
      closeOverlay(dismiss.closest(".cb-modal, .cb-drawer"));
      return;
    }
    if (e.target.classList.contains("cb-scrim")) {
      var overlay = e.target.nextElementSibling;
      closeOverlay(overlay);
    }
  });
  document.addEventListener("keydown", function (e) {
    if (e.key === "Escape") {
      $all(".cb-modal.is-open, .cb-drawer.is-open").forEach(closeOverlay);
      closeAllMenus();
    }
  });

  /* ----- Tabs: .cb-tabs with [data-cb-tab="#panelId"] ----- */
  document.addEventListener("click", function (e) {
    var tab = e.target.closest("[data-cb-tab]");
    if (!tab) return;
    e.preventDefault();
    var group = tab.closest(".cb-tabs");
    var panelId = tab.getAttribute("data-cb-tab").replace(/^#/, "");
    $all(".cb-tab", group).forEach(function (t) { t.classList.toggle("is-active", t === tab); });
    var panel = document.getElementById(panelId);
    if (panel && panel.parentElement) {
      $all(".cb-tab-panel", panel.parentElement).forEach(function (p) {
        p.classList.toggle("is-active", p === panel);
      });
    }
  });

  /* ----- Switch / pill toggle: [data-cb-toggle="pill"] ----- */
  document.addEventListener("click", function (e) {
    var pill = e.target.closest('.cb-pill[data-cb-toggle="pill"]');
    if (pill) pill.classList.toggle("is-selected");
  });

  /* ----- Sortable table: <table class="cb-table" data-cb-sortable> ----- */
  function sortTable(table, th) {
    var idx = Array.prototype.indexOf.call(th.parentElement.children, th);
    var current = th.getAttribute("data-cb-sort") || "none";
    var dir = current === "asc" ? "desc" : "asc";
    $all("thead th", table).forEach(function (h) {
      h.removeAttribute("data-cb-sort"); h.classList.remove("is-sorted");
      var a = h.querySelector(".cb-sort-arrow"); if (a) a.textContent = "";
    });
    th.setAttribute("data-cb-sort", dir); th.classList.add("is-sorted");
    var arrow = th.querySelector(".cb-sort-arrow");
    if (arrow) arrow.textContent = dir === "asc" ? "↑" : "↓";
    var tbody = table.querySelector("tbody");
    var rows = $all("tr", tbody);
    var numeric = th.classList.contains("cb-table-num");
    rows.sort(function (a, b) {
      var av = (a.children[idx].getAttribute("data-cb-value") || a.children[idx].textContent).trim();
      var bv = (b.children[idx].getAttribute("data-cb-value") || b.children[idx].textContent).trim();
      var r;
      if (numeric) r = (parseFloat(av.replace(/[^0-9.\-]/g, "")) || 0) - (parseFloat(bv.replace(/[^0-9.\-]/g, "")) || 0);
      else r = av.localeCompare(bv);
      return dir === "asc" ? r : -r;
    });
    rows.forEach(function (r) { tbody.appendChild(r); });
  }
  document.addEventListener("click", function (e) {
    var th = e.target.closest(".cb-table[data-cb-sortable] thead th.is-sortable");
    if (th) sortTable(th.closest("table"), th);
  });

  /* ----- Row selection: tr in .cb-table-selectable ----- */
  document.addEventListener("click", function (e) {
    var row = e.target.closest(".cb-table-selectable tbody tr");
    if (row && !e.target.closest("a, button, input")) row.classList.toggle("is-selected");
  });

  /* ----- Toasts: Cimbyte.toast(message, { type, timeout }) ----- */
  function ensureToastHost() {
    var host = $(".cb-toast-host");
    if (!host) { host = document.createElement("div"); host.className = "cb-toast-host"; document.body.appendChild(host); }
    return host;
  }
  function toast(message, opts) {
    opts = opts || {};
    var host = ensureToastHost();
    var el = document.createElement("div");
    el.className = "cb-toast" + (opts.type ? " cb-toast-" + opts.type : "");
    el.textContent = message;
    host.appendChild(el);
    var timeout = opts.timeout == null ? 3200 : opts.timeout;
    if (timeout) setTimeout(function () {
      el.classList.add("is-leaving");
      setTimeout(function () { el.remove(); }, 200);
    }, timeout);
    return el;
  }

  window.Cimbyte = {
    toast: toast,
    open: openOverlay,
    close: function (id) { closeOverlay(document.getElementById(id)); }
  };
})();
