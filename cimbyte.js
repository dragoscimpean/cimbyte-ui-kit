/* ==========================================================================
   Cimbyte UI — vanilla behaviors. No dependencies, no framework.
   Drop in <script src="cimbyte.js"></script>. Driven by data-cb-* attributes.
   Exposes window.Cimbyte for imperative use (toasts, open/close).
   ========================================================================== */
(function () {
  "use strict";

  function $(sel, root) { return (root || document).querySelector(sel); }
  function $all(sel, root) { return Array.prototype.slice.call((root || document).querySelectorAll(sel)); }

  var nextId = 0;
  var overlayOpeners = new WeakMap();

  function ensureId(el, prefix) {
    if (el.id) return el.id;
    var id;
    do { id = prefix + (++nextId); } while (document.getElementById(id));
    el.id = id;
    return id;
  }

  function isDisabled(el) {
    return el.disabled || el.getAttribute("aria-disabled") === "true";
  }

  function isVisible(el) {
    return !el.hidden && el.getAttribute("aria-hidden") !== "true" &&
      !!(el.offsetWidth || el.offsetHeight || el.getClientRects().length);
  }

  function focusableElements(root) {
    return $all('a[href], area[href], button:not([disabled]), input:not([disabled]):not([type="hidden"]), select:not([disabled]), textarea:not([disabled]), [tabindex]:not([tabindex="-1"]), [contenteditable="true"]', root)
      .filter(function (el) { return !isDisabled(el) && isVisible(el); });
  }

  /* ----- Dropdown: [data-cb-toggle="dropdown"] toggles the next .cb-menu ----- */
  function dropdownMenu(toggle) {
    var parent = toggle.closest(".cb-dropdown") || toggle.parentElement;
    return parent ? $(".cb-menu", parent) : null;
  }

  function dropdownToggle(menu) {
    var parent = menu.closest(".cb-dropdown") || menu.parentElement;
    return parent ? $('[data-cb-toggle="dropdown"]', parent) : null;
  }

  function menuItems(menu) {
    return $all(".cb-menu-item, [role=\"menuitem\"]", menu)
      .filter(function (item) { return !isDisabled(item) && isVisible(item); });
  }

  function prepareDropdown(toggle) {
    var menu = dropdownMenu(toggle);
    if (!menu) return null;

    var menuId = ensureId(menu, "cb-menu-");
    toggle.setAttribute("aria-haspopup", "menu");
    toggle.setAttribute("aria-controls", menuId);
    toggle.setAttribute("aria-expanded", menu.classList.contains("is-open") ? "true" : "false");
    menu.setAttribute("role", "menu");
    if (!menu.hasAttribute("aria-label") && !menu.hasAttribute("aria-labelledby")) {
      menu.setAttribute("aria-labelledby", ensureId(toggle, "cb-menu-toggle-"));
    }
    menu.setAttribute("aria-hidden", menu.classList.contains("is-open") ? "false" : "true");

    $all(".cb-menu-item", menu).forEach(function (item) {
      if (!item.hasAttribute("role")) item.setAttribute("role", "menuitem");
      item.setAttribute("tabindex", "-1");
      if (item.disabled) item.setAttribute("aria-disabled", "true");
    });
    $all(".cb-menu-sep", menu).forEach(function (separator) {
      if (!separator.hasAttribute("role")) separator.setAttribute("role", "separator");
    });
    return menu;
  }

  function openMenu(toggle, focusAt) {
    if (isDisabled(toggle)) return;
    var menu = prepareDropdown(toggle);
    if (!menu) return;
    closeAllMenus(menu);
    menu.classList.add("is-open");
    menu.setAttribute("aria-hidden", "false");
    toggle.setAttribute("aria-expanded", "true");

    if (focusAt) {
      var items = menuItems(menu);
      var item = focusAt === "last" ? items[items.length - 1] : items[0];
      if (item) item.focus();
    }
  }

  function closeMenu(menu, restoreFocus) {
    if (!menu) return;
    menu.classList.remove("is-open");
    menu.setAttribute("aria-hidden", "true");
    var toggle = dropdownToggle(menu);
    if (toggle) {
      toggle.setAttribute("aria-expanded", "false");
      if (restoreFocus) toggle.focus();
    }
  }

  function closeAllMenus(except) {
    $all(".cb-menu.is-open").forEach(function (menu) {
      if (menu !== except) closeMenu(menu, false);
    });
  }

  document.addEventListener("click", function (e) {
    var toggle = e.target.closest('[data-cb-toggle="dropdown"]');
    if (toggle) {
      e.preventDefault();
      var menu = prepareDropdown(toggle);
      if (menu) {
        if (menu.classList.contains("is-open")) closeMenu(menu, false);
        else openMenu(toggle);
      }
      return;
    }
    if (!e.target.closest(".cb-menu")) closeAllMenus();
  });

  document.addEventListener("keydown", function (e) {
    var toggle = e.target.closest('[data-cb-toggle="dropdown"]');
    if (toggle && (e.key === "ArrowDown" || e.key === "ArrowUp")) {
      e.preventDefault();
      openMenu(toggle, e.key === "ArrowUp" ? "last" : "first");
      return;
    }

    var menu = e.target.closest(".cb-menu.is-open");
    if (!menu && e.key === "Escape") {
      var openMenus = $all(".cb-menu.is-open");
      menu = openMenus[openMenus.length - 1];
    }
    if (!menu) return;

    if (e.key === "Escape") {
      e.preventDefault();
      closeMenu(menu, true);
      return;
    }
    if (e.key === "Tab") {
      closeMenu(menu, false);
      return;
    }

    var items = menuItems(menu);
    if (!items.length) return;
    var current = items.indexOf(document.activeElement);
    var next = current;
    if (e.key === "ArrowDown") next = current < 0 ? 0 : (current + 1) % items.length;
    else if (e.key === "ArrowUp") next = current < 0 ? items.length - 1 : (current - 1 + items.length) % items.length;
    else if (e.key === "Home") next = 0;
    else if (e.key === "End") next = items.length - 1;
    else return;
    e.preventDefault();
    items[next].focus();
  });

  /* ----- Modal / drawer: data-cb-toggle="modal" data-cb-target="#id" ----- */
  function prepareOverlay(el) {
    if (!el) return;
    if (!el.hasAttribute("role")) el.setAttribute("role", "dialog");
    el.setAttribute("aria-modal", "true");
    el.setAttribute("aria-hidden", el.classList.contains("is-open") ? "false" : "true");

    var title = $(".cb-modal-title", el);
    if (title && !el.hasAttribute("aria-label") && !el.hasAttribute("aria-labelledby")) {
      el.setAttribute("aria-labelledby", ensureId(title, "cb-overlay-title-"));
    }
  }

  function prepareOverlayToggle(opener) {
    var target = opener.getAttribute("data-cb-target");
    if (!target) return;
    var id = target.replace(/^#/, "");
    var overlay = document.getElementById(id);
    opener.setAttribute("aria-haspopup", "dialog");
    opener.setAttribute("aria-controls", id);
    opener.setAttribute("aria-expanded", overlay && overlay.classList.contains("is-open") ? "true" : "false");
  }

  function setOverlayToggleState(el, expanded) {
    if (!el.id) return;
    $all('[data-cb-toggle="modal"], [data-cb-toggle="drawer"]').forEach(function (opener) {
      var target = opener.getAttribute("data-cb-target");
      if (target && target.replace(/^#/, "") === el.id) {
        opener.setAttribute("aria-expanded", expanded ? "true" : "false");
      }
    });
  }

  function enterOverlay(el) {
    var autofocus = $("[autofocus]", el);
    var focusables = focusableElements(el);
    var target = autofocus && isVisible(autofocus) ? autofocus : focusables[0];
    if (!target) {
      if (!el.hasAttribute("tabindex")) el.setAttribute("tabindex", "-1");
      target = el;
    }
    target.focus();
  }

  function openOverlay(id, opener) {
    var el = document.getElementById(id);
    if (!el) return;
    prepareOverlay(el);
    var active = opener || document.activeElement;
    if (active && active !== document.body && !el.contains(active)) overlayOpeners.set(el, active);
    el.classList.add("is-open");
    el.setAttribute("aria-hidden", "false");
    setOverlayToggleState(el, true);
    var scrim = el.previousElementSibling;
    if (scrim && scrim.classList.contains("cb-scrim")) {
      scrim.classList.add("is-open");
      scrim.setAttribute("aria-hidden", "true");
    }
    window.setTimeout(function () {
      if (el.classList.contains("is-open")) enterOverlay(el);
    }, 0);
  }

  function closeOverlay(el) {
    if (!el) return;
    el.classList.remove("is-open");
    el.setAttribute("aria-hidden", "true");
    setOverlayToggleState(el, false);
    var scrim = el.previousElementSibling;
    if (scrim && scrim.classList.contains("cb-scrim")) scrim.classList.remove("is-open");
    var opener = overlayOpeners.get(el);
    overlayOpeners.delete(el);
    if (opener && document.documentElement.contains(opener) && !isDisabled(opener)) opener.focus();
  }

  function topOpenOverlay() {
    var overlays = $all(".cb-modal.is-open, .cb-drawer.is-open");
    return overlays[overlays.length - 1] || null;
  }

  document.addEventListener("click", function (e) {
    var opener = e.target.closest('[data-cb-toggle="modal"],[data-cb-toggle="drawer"]');
    if (opener) {
      e.preventDefault();
      prepareOverlayToggle(opener);
      var target = opener.getAttribute("data-cb-target");
      if (target) openOverlay(target.replace(/^#/, ""), opener);
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
    if (e.defaultPrevented) return;
    var overlay = topOpenOverlay();
    if (!overlay) return;

    if (e.key === "Escape") {
      e.preventDefault();
      closeOverlay(overlay);
      return;
    }
    if (e.key !== "Tab") return;

    var focusables = focusableElements(overlay);
    if (!focusables.length) {
      e.preventDefault();
      if (!overlay.hasAttribute("tabindex")) overlay.setAttribute("tabindex", "-1");
      overlay.focus();
      return;
    }

    var first = focusables[0];
    var last = focusables[focusables.length - 1];
    var active = document.activeElement;
    if (e.shiftKey && (active === first || !overlay.contains(active))) {
      e.preventDefault();
      last.focus();
    } else if (!e.shiftKey && (active === last || !overlay.contains(active))) {
      e.preventDefault();
      first.focus();
    }
  });

  /* ----- Tabs: .cb-tabs with [data-cb-tab="#panelId"] ----- */
  function tabPanel(tab) {
    var target = tab.getAttribute("data-cb-tab");
    return target ? document.getElementById(target.replace(/^#/, "")) : null;
  }

  function setTabState(tab, active) {
    tab.classList.toggle("is-active", active);
    tab.setAttribute("aria-selected", active ? "true" : "false");
    tab.setAttribute("tabindex", active ? "0" : "-1");
    var panel = tabPanel(tab);
    if (panel) {
      panel.classList.toggle("is-active", active);
      panel.hidden = !active;
      panel.setAttribute("aria-hidden", active ? "false" : "true");
    }
  }

  function prepareTabGroup(group) {
    if (!group.hasAttribute("role")) group.setAttribute("role", "tablist");
    var tabs = $all(".cb-tab[data-cb-tab]", group);
    var selected = tabs.filter(function (tab) { return tab.classList.contains("is-active"); })[0] || tabs[0];

    tabs.forEach(function (tab) {
      if (!tab.hasAttribute("role")) tab.setAttribute("role", "tab");
      var panel = tabPanel(tab);
      if (panel) {
        var panelId = ensureId(panel, "cb-tab-panel-");
        var tabId = ensureId(tab, "cb-tab-");
        tab.setAttribute("aria-controls", panelId);
        if (!panel.hasAttribute("role")) panel.setAttribute("role", "tabpanel");
        if (!panel.hasAttribute("aria-labelledby")) panel.setAttribute("aria-labelledby", tabId);
      }
      setTabState(tab, tab === selected);
    });
  }

  function activateTab(tab, focus) {
    var group = tab.closest(".cb-tabs");
    if (!group || isDisabled(tab)) return;
    prepareTabGroup(group);
    $all(".cb-tab[data-cb-tab]", group).forEach(function (candidate) {
      setTabState(candidate, candidate === tab);
    });
    if (focus) tab.focus();
  }

  document.addEventListener("click", function (e) {
    var tab = e.target.closest("[data-cb-tab]");
    if (!tab) return;
    e.preventDefault();
    activateTab(tab, false);
  });

  document.addEventListener("keydown", function (e) {
    var tab = e.target.closest(".cb-tab[data-cb-tab]");
    if (!tab) return;
    var group = tab.closest(".cb-tabs");
    if (!group) return;
    var tabs = $all(".cb-tab[data-cb-tab]", group).filter(function (candidate) { return !isDisabled(candidate); });
    if (!tabs.length) return;

    var vertical = group.getAttribute("aria-orientation") === "vertical";
    var current = tabs.indexOf(tab);
    var next = current;
    if (e.key === "Home") next = 0;
    else if (e.key === "End") next = tabs.length - 1;
    else if ((!vertical && e.key === "ArrowRight") || (vertical && e.key === "ArrowDown")) next = (current + 1) % tabs.length;
    else if ((!vertical && e.key === "ArrowLeft") || (vertical && e.key === "ArrowUp")) next = (current - 1 + tabs.length) % tabs.length;
    else return;
    e.preventDefault();
    activateTab(tabs[next], true);
  });

  /* ----- Switch / pill toggle: [data-cb-toggle="pill"] ----- */
  function preparePill(pill) {
    if (pill.tagName !== "BUTTON") {
      if (!pill.hasAttribute("role")) pill.setAttribute("role", "button");
      if (!pill.hasAttribute("tabindex")) pill.setAttribute("tabindex", "0");
    }
    pill.setAttribute("aria-pressed", pill.classList.contains("is-selected") ? "true" : "false");
  }

  function togglePill(pill) {
    if (isDisabled(pill)) return;
    preparePill(pill);
    var selected = !pill.classList.contains("is-selected");
    pill.classList.toggle("is-selected", selected);
    pill.setAttribute("aria-pressed", selected ? "true" : "false");
  }

  document.addEventListener("click", function (e) {
    var pill = e.target.closest('.cb-pill[data-cb-toggle="pill"]');
    if (pill) togglePill(pill);
  });

  document.addEventListener("keydown", function (e) {
    var pill = e.target.closest('.cb-pill[data-cb-toggle="pill"]');
    if (!pill || pill.tagName === "BUTTON" || (e.key !== "Enter" && e.key !== " ")) return;
    e.preventDefault();
    togglePill(pill);
  });

  /* ----- Sortable table: <table class="cb-table" data-cb-sortable> ----- */
  function prepareTable(table) {
    if (table.hasAttribute("data-cb-sortable")) {
      $all("thead th.is-sortable", table).forEach(function (th) {
        th.setAttribute("scope", "col");
        th.setAttribute("tabindex", "0");
        var sort = th.getAttribute("data-cb-sort");
        th.setAttribute("aria-sort", sort === "asc" ? "ascending" : sort === "desc" ? "descending" : "none");
      });
    }
    if (table.classList.contains("cb-table-selectable")) {
      $all("tbody tr", table).forEach(function (row) {
        row.setAttribute("tabindex", "0");
        row.setAttribute("aria-selected", row.classList.contains("is-selected") ? "true" : "false");
      });
    }
  }

  function sortTable(table, th) {
    prepareTable(table);
    var idx = Array.prototype.indexOf.call(th.parentElement.children, th);
    var current = th.getAttribute("data-cb-sort") || "none";
    var dir = current === "asc" ? "desc" : "asc";
    $all("thead th", table).forEach(function (header) {
      header.removeAttribute("data-cb-sort");
      header.classList.remove("is-sorted");
      if (header.classList.contains("is-sortable")) header.setAttribute("aria-sort", "none");
      var arrow = header.querySelector(".cb-sort-arrow");
      if (arrow) arrow.textContent = "";
    });
    th.setAttribute("data-cb-sort", dir);
    th.setAttribute("aria-sort", dir === "asc" ? "ascending" : "descending");
    th.classList.add("is-sorted");
    var arrow = th.querySelector(".cb-sort-arrow");
    if (arrow) arrow.textContent = dir === "asc" ? "↑" : "↓";
    var tbody = table.querySelector("tbody");
    var rows = $all("tr", tbody);
    var numeric = th.classList.contains("cb-table-num");
    rows.sort(function (a, b) {
      var av = (a.children[idx].getAttribute("data-cb-value") || a.children[idx].textContent).trim();
      var bv = (b.children[idx].getAttribute("data-cb-value") || b.children[idx].textContent).trim();
      var result;
      if (numeric) result = (parseFloat(av.replace(/[^0-9.\-]/g, "")) || 0) - (parseFloat(bv.replace(/[^0-9.\-]/g, "")) || 0);
      else result = av.localeCompare(bv);
      return dir === "asc" ? result : -result;
    });
    rows.forEach(function (row) { tbody.appendChild(row); });
  }

  document.addEventListener("click", function (e) {
    var th = e.target.closest(".cb-table[data-cb-sortable] thead th.is-sortable");
    if (th) sortTable(th.closest("table"), th);
  });

  document.addEventListener("keydown", function (e) {
    var th = e.target.closest(".cb-table[data-cb-sortable] thead th.is-sortable");
    if (!th || (e.key !== "Enter" && e.key !== " ")) return;
    e.preventDefault();
    sortTable(th.closest("table"), th);
  });

  /* ----- Row selection: tr in .cb-table-selectable ----- */
  function toggleRow(row) {
    var selected = !row.classList.contains("is-selected");
    row.classList.toggle("is-selected", selected);
    row.setAttribute("aria-selected", selected ? "true" : "false");
  }

  document.addEventListener("click", function (e) {
    var row = e.target.closest(".cb-table-selectable tbody tr");
    if (row && !e.target.closest("a, button, input, select, textarea, [role=button]")) toggleRow(row);
  });

  document.addEventListener("keydown", function (e) {
    var row = e.target.closest(".cb-table-selectable tbody tr");
    if (!row || e.target !== row || (e.key !== "Enter" && e.key !== " ")) return;
    e.preventDefault();
    toggleRow(row);
  });

  /* ----- Toasts: Cimbyte.toast(message, { type, timeout }) ----- */
  function ensureToastHost() {
    var host = $(".cb-toast-host");
    if (!host) {
      host = document.createElement("div");
      host.className = "cb-toast-host";
      document.body.appendChild(host);
    }
    if (!host.hasAttribute("role")) host.setAttribute("role", "region");
    if (!host.hasAttribute("aria-label")) host.setAttribute("aria-label", "Notifications");
    return host;
  }

  function toast(message, opts) {
    opts = opts || {};
    var host = ensureToastHost();
    var el = document.createElement("div");
    el.className = "cb-toast" + (opts.type ? " cb-toast-" + opts.type : "");
    el.setAttribute("role", opts.type === "error" ? "alert" : "status");
    el.setAttribute("aria-live", opts.type === "error" ? "assertive" : "polite");
    el.setAttribute("aria-atomic", "true");
    el.textContent = message;
    host.appendChild(el);
    var timeout = opts.timeout == null ? 3200 : opts.timeout;
    if (timeout) window.setTimeout(function () {
      el.classList.add("is-leaving");
      window.setTimeout(function () { el.remove(); }, 200);
    }, timeout);
    return el;
  }

  function initializeAccessibility() {
    $all('[data-cb-toggle="dropdown"]').forEach(prepareDropdown);
    $all('[data-cb-toggle="modal"], [data-cb-toggle="drawer"]').forEach(prepareOverlayToggle);
    $all(".cb-modal, .cb-drawer").forEach(prepareOverlay);
    $all(".cb-scrim").forEach(function (scrim) { scrim.setAttribute("aria-hidden", "true"); });
    $all(".cb-tabs").forEach(prepareTabGroup);
    $all('.cb-pill[data-cb-toggle="pill"]').forEach(preparePill);
    $all(".cb-table").forEach(prepareTable);
  }

  if (document.readyState === "loading") document.addEventListener("DOMContentLoaded", initializeAccessibility);
  else initializeAccessibility();

  window.Cimbyte = {
    toast: toast,
    open: openOverlay,
    close: function (id) { closeOverlay(document.getElementById(id)); }
  };
})();
