import { getGpxListAPI, postGpxFilePathAPI, putGpxAPI } from '@/api/index';
import { GpxMap } from '@/cesium/cesium';
import { Handler } from '@/cesium/handler';
import * as Cesium from 'cesium';
import { reactive } from 'vue';

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
  });
  
  isShowAlert = false;
  isLoaded = false;
  alertMessage = '';
  loadingMessage = '';
  gpxFilenames = [];
  gpxMaps = [];
  nowToolMode = 0;

  static getInstance() {
    if (!eventManager.instance) {
      eventManager.instance = new eventManager();
    }
    return eventManager.instance;
  }

  // cesium event
  setViewer(viewer) {
    this.viewer = viewer;
    return this;
  }

  buildGpxMaps() {
    getGpxListAPI().then(async (res) => {
      this.showLoadingPercent(60, 'GPX analyzing...');
      res = res.data.result;
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
      });
    });
    return this;
  }

  initializeHandler() {
    this.handler = new Handler(this.viewer);
    this.handler.initialize();
    return this;
  }

  addWayPoint(pointData) {
    if (this.state.nowSelectMap === -1) {
      // TODO
      return;
    }
    this.gpxMaps[this.state.nowSelectMap].addPoint('wayPoints', pointData);
    this.toggleWayPointRequest(-1);
  }

  removePoint(mapIdx, pointId) {
    this.gpxMaps[mapIdx].removePoint(pointId);
  }

  reDrawLine(id) {
    this.gpxMaps.forEach((gpxMap) => {
      if (gpxMap.isHasPoint(id)) {
        gpxMap.reDrawLine();
      }
    });
  }

  // Handler event
  callClick(pick, cartesian) {
    if (this.nowToolMode === ToolBar.NORMAL) {
      // TODO
    } else if (this.nowToolMode === ToolBar.ADDWAYPOINT) {
      this.addWayPoint(cartesian);
    }
  }

  // Init vue
  postGPXFile(filenames) {
    this.addGpxMaps(filenames);
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
    // TODO
    this.gpxMaps.forEach((gpxMap) => {
      if (gpxMap.isHasPoint(id)) {
        gpxMap.updatePoint(id, gpxPoint);
      }
    });
  }

  addGpxMaps(filenames) {
    filenames = filenames.filter((filename) => {
      return this.gpxFilenames.indexOf(filename) === -1;
    });
    filenames.forEach((filename) => {
      this.gpxFilenames.push(filename);
    });
    postGpxFilePathAPI(filenames);
  }

  saveGpxMaps() {
    let postDatas = [];
    this.gpxMaps.forEach((gpxMap) => {
      postDatas.push(gpxMap.toJson());
    });
    putGpxAPI(postDatas);
  }

  selectMap(index, toggle = true) {
    if (this.state.nowSelectMap !== -1 && this.state.nowSelectMap !== index ) {
      this.unhover(this.state.nowSelectMap);
    }
    if (index >= -1 && index <= this.gpxMaps.length) {
      if (this.state.nowSelectMap !== index || !toggle) {
        this.state.nowSelectMap = index;
        this.state.nowSelectType = SelectType.MAP;
        this.hover(index);
      } else {
        this.state.nowSelectMap = -1;
        this.state.nowSelectType = SelectType.NONE;
      }
    }
  }

  selectPoint(index, pointId) {
    this.state.nowSelectType = SelectType.POINT;
    this.state.nowSelectPoint = pointId;
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
    }
  }

  toggleWayPointRequest(mapIdx) {
    if (this.nowToolMode !== ToolBar.ADDWAYPOINT) {
      this.changeMode(ToolBar.ADDWAYPOINT);
      this.selectMap(mapIdx);
    } else {
      this.changeMode(ToolBar.NORMAL);
      this.selectMap(-1);
    }
  }

  // Toolbar event

  setTracking(mapIdx) {
    this.gpxMaps[mapIdx].activeTime();
  }

  beginTracking() {
    this.viewer.clock.shouldAnimate = true;
  }

  stopTracking() {
    this.viewer.clock.shouldAnimate = false;
  }

  setTrakingSpeed(speed) {
    console.log(speed);
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
  }

  zoomIn(){
    this.viewer.camera.zoomIn();
  }

  zoomOut(){
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
  }

  unhover(mapIdx) {
    this.gpxMaps[mapIdx].unhover();
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
