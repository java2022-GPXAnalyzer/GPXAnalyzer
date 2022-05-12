const {app, BrowserWindow} = require('electron');
const path = require('path');

function createWindow() {
  const mainWindow = new BrowserWindow({
    width: 800,
    height: 600,
    webPreferences:{
      preload: path.join(__dirname, 'preload.js'),
    }
  });

  // mainWindow.loadFile('dist/index.html');
  mainWindow.loadURL(
    process.env.NODE_ENV === 'development' ?
      'http://127.0.0.1:3000/' :
      `file://${path.join(__dirname, '../dist/index.html')}`
    )

  // Open the DevTools.
  if (process.env.NODE_ENV === 'development') {
    mainWindow.webContents.openDevTools();
  }
}

app.whenReady().then(() => {
  createWindow();

  app.on('activate', function () {
    if (BrowserWindow.getAllWindows().length === 0) createWindow();
  });
});

app.on('window-all-closed', function () {
  if (process.platform !== 'darwin') app.quit();
});