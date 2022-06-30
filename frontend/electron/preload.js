const { ipcRenderer, contextBridge } = require('electron');
const { spawn, exec } = require('child_process')
const path = require('path')

contextBridge.exposeInMainWorld('$electron', {
  ipcRenderer: ipcRenderer
});

contextBridge.exposeInMainWorld('backendAPI', {
  startServerForSpawn: () => {
    let jarPath, jrePath;
    if (process.env.NODE_ENV === 'development'){
      jarPath = '../backendApp/GPXAnalayzer.jar';
      jrePath = './jre/bin/java';
    }
    else {
      jarPath = '../../backendApp/GPXAnalayzer.jar';
      jrePath = path.join(__dirname, '../../jre/bin/java');
    }
    let path1 = path.join(__dirname, jarPath);
    const ls = spawn(jrePath, ['-jar', path1]);
    // console.log(path1, jrePath);
    ls.stdout.on('data', (data) => {
      if(data.toString().indexOf("Starting GpxAnalayzerApplication") !== -1){
        // window.location.href="http://localhost:80";
        console.log("Server started");
      }
    });
    ls.stderr.on('data', (data) => {
      console.error(`stderr: ${data}`);
      alert("啟動後端失敗，請重啟。");
    });
    ls.on('close', (code) => {
      console.log(`child process exited with code ${code}`);
    });
  },
  startServerForbat: () =>{
    const bat = spawn(path.join(__dirname, '../backendApp/backend.bat'));
    bat.stdout.on('data', (data) => {
      console.log(data.toString());
      if(data.toString().indexOf("Starting GpxAnalayzerApplication") !== -1){
        // window.location.href="http://localhost:80";
      }
    });
    bat.stderr.on('data', (data) => {
      console.error(data.toString());
      alert("啟動後端失敗，請重啟。");
    });
    bat.on('exit', (code) => {
      console.log(`Child exited with code ${code}`);
    });
  }
});


window.addEventListener('DOMContentLoaded', () => {
  const replaceText = (selector, text) => {
    const element = document.getElementById(selector);
    if (element) element.innerText = text;
  };

  for (const dependency of ['chrome', 'node', 'electron']) {
    replaceText(`${dependency}-version`, process.versions[dependency]);
  }
});
