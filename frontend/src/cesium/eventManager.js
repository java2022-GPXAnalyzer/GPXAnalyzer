import { getGpxListAPI, postGpxFilePathAPI, putGpxAPI } from '@/api/index';
import { GpxMap } from '@/cesium/cesium';
import { Handler } from '@/cesium/handler';
import * as Cesium from 'cesium';
import { reactive } from 'vue';

const ToolBar = {
  NORMAL: 0,
  ADDWAYPOINT: 1,
}

const SelectType = {
  NONE: 0,
  MAP: 1,
  POINT: 2
}

export class eventManager{
  state = reactive({
    gpxMaps: [],
    showAlertMode: 0,
    showLoading: false,
    loadingPercentMax: 0
  });
  
  isShowAlert = false;
  isLoaded = false;
  alertMessage = '';
  loadingMessage = '';
  gpxFilenames = [];
  gpxMaps = [];
  nowSelectMap = -1;
  nowToolMode = 0;
  nowSelectType = 0;

  static getInstance(){
    if(!eventManager.instance){
      eventManager.instance = new eventManager();
    }
    return eventManager.instance;
  }

  // cesium event
  setViewer(viewer){
    this.viewer = viewer;
    return this;
  }

  buildGpxMaps(){
    getGpxListAPI().then((res) => {
      this.showLoadingPercent(65, 'GPX analyzing...');
      res = res.data.result;
      res.forEach((uuid, index) => {
        let gpxMap = new GpxMap(this.viewer, uuid, index);
        gpxMap.initialize();
        this.state.gpxMaps.push(gpxMap);
      })
      this.showLoadingPercent(100, 'GPX loading...');
    });
    return this;
  }

  initializeHandler(){
    this.handler = new Handler(this.viewer);
    this.handler.initialize();
    return this;
  }

  addWayPoint(pointData){
    if(this.nowSelectMap === -1){
      // TODO
      return;
    }
    this.state.gpxMaps[this.nowSelectMap].addPoint("wayPoints", pointData);
    this.toggleWayPointRequest(-1);
  }

  removePoint(mapIdx, pointId){
    this.state.gpxMaps[mapIdx].removePoint(pointId);
  }

  reDrawLine(id){
    this.state.gpxMaps.forEach((gpxMap) => {
      if(gpxMap.isHasPoint(id)){
        gpxMap.reDrawLine();
      }
    });
  }

  // Handler event
  callClick(pick, cartesian){
    if(this.nowToolMode === ToolBar.NORMAL){
      // TODO
    }
    else if(this.nowToolMode === ToolBar.ADDWAYPOINT){
      this.addWayPoint(cartesian);
    }
  }

  // Init vue
  postGPXFile(filenames){
    this.addGpxMaps(filenames);
  }

  getGpxMaps(){
    return this.state.gpxMaps;
  }

  setGpxMaps(gpxMaps){
    this.state.gpxMaps = gpxMaps;
  }

  // index vue event
  updateGpxInfo(gpxInfo){
    this.state.gpxMaps[this.nowSelectMap].updateGpxInfo(gpxInfo);
  }

  updateGpxPoint(id, gpxPoint){
    // TODO
    this.state.gpxMaps.forEach((gpxMap) => {
      if(gpxMap.isHasPoint(id)){
        gpxMap.updatePoint(id, gpxPoint);
      }
    });
  }

  addGpxMaps(filenames){
    filenames = filenames.filter((filename) =>{
      return this.gpxFilenames.indexOf(filename) === -1;
    })
    filenames.forEach((filename) => {
      this.gpxFilenames.push(filename);
    });
    postGpxFilePathAPI(filenames);
  }

  saveGpxMaps(){
    let postDatas = [];
    this.state.gpxMaps.forEach((gpxMap) => {
      postDatas.push(gpxMap.toJson());
    });
    putGpxAPI(postDatas);
  }

  selectMap(index){
    if(index >= -1 && index <= this.state.gpxMaps.length){
      this.nowSelectMap = index;
      this.nowSelectType = SelectType.MAP;
    }
  }

  selectPoint(mapIdx, pointId){
    this.nowSelectType = SelectType.POINT;
    this.nowSelectMap = mapIdx;
    this.nowSelectPoint = pointId;
  }

  getInfo(){
    if(this.nowSelectType === SelectType.MAP){
      return {
        type: SelectType.MAP,
        result: this.state.gpxMaps[this.nowSelectMap]
      };
    }
    else if(this.nowSelectType === SelectType.POINT){
      return {
        type: SelectType.POINT,
        result: this.state.gpxMaps[this.nowSelectMap].getPoint(this.nowSelectPoint)
      };
    }
    return{
      type: SelectType.NONE,
      result: null
    }
  }

  changeMode(mode){
    if(mode >= 0 && mode <= 1){
      this.nowToolMode = mode;
    }
  }

  toggleWayPointRequest(mapIdx){
    if(this.nowToolMode !== ToolBar.ADDWAYPOINT){
      this.changeMode(ToolBar.ADDWAYPOINT);
      this.selectMap(mapIdx);
    }
    else{
      this.changeMode(ToolBar.NORMAL);
      this.selectMap(-1);
    }
  }

  // Toolbar event

  setTracking(mapIdx){
    this.state.gpxMaps[mapIdx].activeTime();
  }

  beginTracking(){
    this.viewer.clock.shouldAnimate = true;
  }

  stopTracking(){
    this.viewer.clock.shouldAnimate = false;
  }

  setTrakingSpeed(speed){
    this.viewer.clock.multiplier = speed;
  }

  fullScreen(){
    Cesium.Fullscreen.requestFullscreen(this.viewer.scene.canvas);
  }

  backToHomeView(){
    this.viewer.trackedEntity = null;
    this.viewer.camera.setView({
      destination: Cesium.Cartesian3.fromDegrees(121.0, 23.5, 550000.0),
    });
  }

  toggleMapDisplay(mapIdx){
    this.state.gpxMaps[mapIdx].toggleDisplay();
  }

  toggleSpeedDistribution(mapIdx){
    let res = this.state.gpxMaps[mapIdx].toggleSpeedDistribution();
    if(!res){
      this.showAlertMessage(2, 'Please save the map first.');
    }
  }

  toggleHover(mapIdx){
    this.state.gpxMaps[mapIdx].toggleHover();
  }

  // alert event
  showAlertMessage(showAlertMode, message){
    this.state.showAlertMode = showAlertMode;
    this.alertMessage = message;
  }

  // Loading Component
  showLoading(){
    this.state.showLoading = true;
  }

  showLoadingPercent(percent, message){
    console.log(percent, message);
    this.state.showLoading = true;
    this.state.loadingPercentMax = percent;
    this.loadingMessage = message;
  }

  hideLoading(){
    this.state.showLoading = false;
  }
}