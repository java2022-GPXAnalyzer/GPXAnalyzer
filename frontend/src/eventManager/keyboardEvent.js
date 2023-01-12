function initKeyboardEvent() {
  window.onkeydown = (e) => {
    let ev = window.event || e;
    let code = ev.keyCode || ev.which;
    if (code == 82 && (ev.metaKey || ev.ctrlKey)) {
      return false;
    }
    if (code == 83 && (ev.metaKey || ev.ctrlKey)) {
      return this.saveGpxMaps();
    }
    if (code == 82 && code == 16 && (ev.metaKey || ev.ctrlKey)) {
      return false;
    }
  };
}

export default initKeyboardEvent;