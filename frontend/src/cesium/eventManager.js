import {
  getGpxListAPI,
  postGpxFilePathAPI,
  putGpxAPI,
  getGpxHotSpotAPI,
} from '@/api/index';
import { GpxMap } from '@/cesium/cesium';
import { Handler } from '@/cesium/handler';
import * as Cesium from 'cesium';
import { reactive } from 'vue';
// const { ipcRenderer } = window.require('electron');
// import { ipcRenderer } from 'electron';

const ToolBar = {
  NORMAL: 0,
  ADDWAYPOINT: 1,
};

const SelectType = {
  NONE: 0,
  MAP: 1,
  POINT: 2,
};

export class eventManager {
  state = reactive({
    gpxLength: 0,
    showAlertMode: 0,
    showLoading: false,
    loadingPercentMax: 0,
    nowSelectMap: -1,
    nowSelectPoint: null,
    nowSelectType: 0,
    isPointChanged: false,
    nowToolMode: 0,
    hotSpot: false
  });

  isShowAlert = false;
  isLoaded = false;
  alertMessage = '';
  loadingMessage = '';
  gpxFilenames = [];
  gpxMaps = [];
  nowToolMode = 0;
  allMapsStartTime = null;
  allMapsEndTime = null;

  static getInstance() {
    if (!eventManager.instance) {
      eventManager.instance = new eventManager();
      eventManager.instance.initKeyboardEvent();
    }
    return eventManager.instance;
  }

  // keyboard event

  initKeyboardEvent() {
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

  // cesium event
  setViewer(viewer) {
    this.viewer = viewer;
    return this;
  }

  _maxTime(a, b) {
    return a > b ? a : b;
  }

  _minTime(a, b) {
    return a < b ? a : b;
  }

  _timeUpdateAllTime() {
    this.allMapsStartTime = null;
    this.allMapsEndTime = null;
    this.gpxMaps.forEach((gpxMap) => {
      if (this.allMapsStartTime)
        this.allMapsStartTime = this._minTime(
          this.allMapsStartTime,
          gpxMap.getStartTimeClone()
        );
      else this.allMapsStartTime = gpxMap.getStartTimeClone();
      if (this.allMapsEndTime)
        this.allMapsEndTime = this._maxTime(
          this.allMapsEndTime,
          gpxMap.getEndTimeClone()
        );
      else this.allMapsEndTime = gpxMap.getEndTimeClone();
    });
  }

  buildGpxMaps() {
    getGpxListAPI().then(async (res) => {
      this.showLoadingPercent(60, 'GPX analyzing...');
      console.log(res);
      res = res.data;
      let mapCount = res.length;
      res.forEach(async (uuid, index) => {
        let gpxMap = new GpxMap(this.viewer, uuid, index);
        await gpxMap.initialize();
        this.gpxMaps.push(gpxMap);
        this.state.gpxLength += 1;
        this.showLoadingPercent(
          60 + (index + 1) * (40 / mapCount),
          'GPX loading...'
        );
        this._timeUpdateAllTime();
        this.activeTime();
      });
    });
    return this;
  }

  initializeHandler() {
    this.handler = new Handler(this.viewer);
    this.handler.initialize();
    return this;
  }

  resetHome() {
    this.activeTime();
    if (this.state.nowSelectPoint !== null) {
      this.gpxMaps[this.state.nowSelectMap]
        .getPoint(this.state.nowSelectPoint)
        .unhover();
    }
    this.state.nowSelectMap = -1;
    this.state.nowSelectPoint = null;
    this.state.nowSelectType = SelectType.NONE;
    this.gpxMaps.forEach((gpxMap) => {
      gpxMap.unhover();
    });
  }

  addWayPoint(pointData) {
    if (this.state.nowSelectMap === -1) {
      this.showAlertMessage(2, 'Please select a map.');
      return;
    }
    let uuid = this.gpxMaps[this.state.nowSelectMap].addPoint(
      'wayPoints',
      pointData
    );
    if (this.state.nowSelectPoint !== null) {
      this.gpxMaps[this.state.nowSelectMap]
        .getPoint(this.state.nowSelectPoint)
        .unhover();
    }
    this.state.isPointChanged = true;
    this.state.nowSelectPoint = uuid;
    this.state.nowSelectType = SelectType.POINT;
    this.toggleWayPointRequest();
  }

  removePoint(mapIdx, pointId) {
    this.state.nowSelectPoint = null;
    this.state.nowSelectType = SelectType.MAP;
    this.state.isPointChanged = true;
    this.gpxMaps[mapIdx].removePoint(pointId);
    this.gpxMaps[mapIdx].reDrawLine();
    this._timeUpdateAllTime();
    this.activeTime();
  }

  reDrawLine(id) {
    this.gpxMaps.forEach((gpxMap) => {
      if (gpxMap.isHasPoint(id)) {
        gpxMap.reDrawLine();
      }
    });
  }

  activeTime() {
    this.viewer.clock.startTime = this.allMapsStartTime.clone();
    this.viewer.clock.stopTime = this.allMapsEndTime.clone();
    this.viewer.clock.currentTime = this.allMapsStartTime.clone();
  }

  // Handler event
  callClick(pick, cartesian) {
    if (this.nowToolMode === ToolBar.NORMAL) {
      // TODO
    } else if (this.nowToolMode === ToolBar.ADDWAYPOINT) {
      this.addWayPoint(cartesian);
    }
  }

  clickPoint(id) {
    for (let i = 0; i < this.gpxMaps.length; i++) {
      if (this.gpxMaps[i].isHasPoint(id)) {
        this.selectMap(i, id);
        this.gpxMaps[i].hover(id);
        return;
      }
    }
  }

  // Init vue
  postGPXFile(filenames) {
    filenames = filenames.filter((filename) => {
      return this.gpxFilenames.indexOf(filename) === -1;
    });
    // console.log(filenames[0], typeof filenames[0]);
    return postGpxFilePathAPI(filenames);
  }

  getGpxMaps() {
    return this.gpxMaps;
  }

  setGpxMaps(gpxMaps) {
    this.gpxMaps = gpxMaps;
  }

  // index vue event
  updateGpxInfo(gpxInfo) {
    this.gpxMaps[this.state.nowSelectMap].updateGpxInfo(gpxInfo);
  }

  updateGpxPoint(id, gpxPoint) {
    this.gpxMaps.forEach((gpxMap) => {
      if (gpxMap.isHasPoint(id)) {
        gpxMap.updatePoint(id, gpxPoint);
      }
    });
  }

  addGpxMaps(filenames) {
    this.postGPXFile(filenames)
      .then((res) => {
        console.log(res);
        res = res.data;
        filenames.forEach((filename) => {
          this.gpxFilenames.push(filename);
        });
        res.forEach(async (uuid) => {
          let gpxMap = new GpxMap(this.viewer, uuid, this.gpxMaps.length);
          await gpxMap.initialize();
          this.gpxMaps.push(gpxMap);
          this.state.gpxLength += 1;
          this._timeUpdateAllTime();
          this.activeTime();
        });
      })
      .catch((e) => {
        console.log(e);
        this.showAlertMessage(2, 'GPX file upload failed.');
      });
  }

  saveGpxMaps() {
    if (this.saveLazy) {
      return;
    }
    this.saveLazy = setTimeout(() => {
      let postDatas = [];
      this.gpxMaps.forEach((gpxMap) => {
        postDatas.push(gpxMap.toJson());
      });
      putGpxAPI(postDatas)
        .then((e) => {
          this.showAlertMessage(1, 'GPX file saved.');
        })
        .catch((e) => {
          this.showAlertMessage(2, 'GPX file save failed.');
        });
      this.saveLazy = null;
    }, 500);
  }

  selectMap(index, point = null) {
    if (
      this.state.nowSelectMap === index &&
      this.state.nowSelectType === SelectType.MAP &&
      !point
    ) {
      this.unhover(this.state.nowSelectMap);
      this.state.nowSelectMap = -1;
      this.state.nowSelectType = SelectType.NONE;
      return;
    }
    if (this.state.nowSelectType !== SelectType.NONE) {
      if (this.state.nowSelectMap !== index) {
        this.unhover(this.state.nowSelectMap);
        this.hover(index);
      }
      if (this.state.nowSelectPoint !== null) {
        this.gpxMaps[this.state.nowSelectMap]
          .getPoint(this.state.nowSelectPoint)
          .unhover();
      }
    } else {
      this.hover(index);
    }
    this.state.nowSelectMap = index;
    this.state.nowSelectPoint = point;
    this.state.nowSelectType = point ? SelectType.POINT : SelectType.MAP;
    if (point) {
      this.gpxMaps[index].getPoint(point).hover();
    }
  }

  getInfo() {
    if (this.state.nowSelectType === SelectType.MAP) {
      return {
        type: SelectType.MAP,
        result: this.gpxMaps[this.state.nowSelectMap],
      };
    } else if (this.state.nowSelectType === SelectType.POINT) {
      return {
        type: SelectType.POINT,
        result: this.gpxMaps[this.state.nowSelectMap].getPoint(
          this.state.nowSelectPoint
        ),
      };
    }
    return {
      type: SelectType.NONE,
      result: null,
    };
  }

  changeMode(mode) {
    if (mode >= 0 && mode <= 1) {
      this.nowToolMode = mode;
      this.state.nowToolMode = mode;
    }
  }

  toggleWayPointRequest() {
    if (this.nowToolMode !== ToolBar.ADDWAYPOINT) {
      this.changeMode(ToolBar.ADDWAYPOINT);
    } else {
      this.changeMode(ToolBar.NORMAL);
    }
  }

  async getHotSpot(){
    let res = await getHotSpotAPI();
    res = res.data;
    return res;
  }

  // Toolbar event

  _compareTime(time1, time2) {
    return (
      time1.dayNumber === time2.dayNumber &&
      time1.secondsOfDay === time2.secondsOfDay
    );
  }

  _checkViewerClock() {
    return this._compareTime(
      this.viewer.clock.startTime,
      this.viewer.clock.stopTime
    );
  }

  setTracking(mapIdx) {
    this.gpxMaps[mapIdx].activeTime();
  }

  beginTracking() {
    if (this._checkViewerClock()) {
      this.showAlertMessage(1, 'Please select other map.');
      return false;
    }
    this.viewer.clock.shouldAnimate = true;
    return true;
  }

  stopTracking() {
    this.viewer.clock.shouldAnimate = false;
  }

  setTrakingSpeed(speed) {
    this.viewer.clock.multiplier = speed;
  }

  fullScreen() {
    Cesium.Fullscreen.requestFullscreen(this.viewer.scene.canvas);
  }

  backToHomeView() {
    this.viewer.trackedEntity = null;
    this.viewer.camera.setView({
      destination: Cesium.Cartesian3.fromDegrees(121.0, 23.5, 550000.0),
    });
    this.resetHome();
  }

  zoomIn() {
    this.viewer.camera.zoomIn();
  }

  zoomOut() {
    this.viewer.camera.zoomOut();
  }

  toggleMapDisplay(mapIdx) {
    this.gpxMaps[mapIdx].toggleDisplay();
  }

  toggleSpeedDistribution() {
    if (this.state.nowSelectMap === -1) {
      this.showAlertMessage(2, 'Please select a map.');
      return;
    }
    let res = this.gpxMaps[this.state.nowSelectMap].toggleSpeedDistribution();
    if (!res) {
      this.showAlertMessage(2, 'Please save the map first.');
    }
  }

  hover(mapIdx) {
    this.gpxMaps[mapIdx].hover();
    this.gpxMaps[mapIdx].activeTime();
  }

  unhover(mapIdx) {
    this.gpxMaps[mapIdx].unhover();
    this.activeTime();
  }

  changeMap(map) {
    var url =
      'https://mt1.google.cn/vt/lyrs=s&hl=zh-TW&x={x}&y={y}&z={z}&s=Gali';
    let google = new Cesium.UrlTemplateImageryProvider({ url: url });

    let bing = new Cesium.BingMapsImageryProvider({
      url: 'https://dev.virtualearth.net',
      culture: 'zh-Hant',
      key: 'AtkX3zhnRe5fyGuLU30uZw8r3sxdBDnpQly7KfFTCB2rGlDgXBG3yr-qEiQEicEc',
    });

    let maps = [bing, google];

    console.log(map);

    // this.viewer.imageryLayers.removeAll();
    this.viewer.imageryLayers.addImageryProvider(maps[map]);
  }

  toggleHotSpot() {
    console.log('toggleHotSpot'); 
    if (this.state.hotSpot === true) {
      this.state.hotSpot = false;
    }
    else {
      this.state.hotSpot = true;
    }
  }

  // alert event
  showAlertMessage(showAlertMode, message) {
    this.state.showAlertMode = showAlertMode;
    this.alertMessage = message;
  }

  // Loading Component
  showLoading() {
    this.state.showLoading = true;
  }

  showLoadingPercent(percent, message) {
    console.log(percent, message);
    this.state.showLoading = true;
    this.state.loadingPercentMax = percent;
    this.loadingMessage = message;
  }

  hideLoading() {
    this.state.showLoading = false;
  }
}
