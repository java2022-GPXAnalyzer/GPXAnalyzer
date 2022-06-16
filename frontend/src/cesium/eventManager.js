import { getGpxListAPI, postGpxFilePathAPI, putGpxAPI } from '@/api/index';
import { GpxMap } from '@/cesium/cesium';
import { Handler } from '@/cesium/handler';

const ToolBar = {
  NORMAL: 0,
  ADDWAYPOINT: 1,
  ADDTRAKPOINT: 2,
}

export class eventManager{
  gpxMaps = [];
  nowSelectMap = 0;
  nowMode = 1;

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
      res = res.data.result;
      res.forEach((uuid, index) => {
        let gpxMap = new GpxMap(this.viewer, uuid, index);
        gpxMap.initialize();
        this.gpxMaps.push(gpxMap);
      })
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
    this.gpxMaps[this.nowSelectMap].addPoint("wayPoints", pointData);
  }

  // Add Trkpoint is so slow
  // addTrkPoint(pointData){
  //   if(this.nowSelectMap === -1){
  //     // TODO
  //     return;
  //   }
  //   this.gpxMaps[this.nowSelectMap].addPoint("trkPoints", pointData);
  // }

  reDrawLine(id){
    this.gpxMaps.forEach((gpxMap) => {
      if(gpxMap.isHasPoint(id)){
        gpxMap.reDrawLine();
      }
    });
  }

  // Handler event
  callClick(pick, cartesian){
    if(this.nowMode === ToolBar.NORMAL){
      // TODO
    }
    else if(this.nowMode === ToolBar.ADDWAYPOINT){
      this.addWayPoint(cartesian);
    }
    // else if(this.nowMode === ToolBar.ADDTRAKPOINT){
    //   this.addTrkPoint(cartesian);
    // }
  }

  // Init vue
  postGPXFile(filenames){
    postGpxFilePathAPI(filenames);
  }

  getGpxMaps(){
    return this.gpxMaps;
  }

  setGpxMaps(gpxMaps){
    this.gpxMaps = gpxMaps;
  }

  // index vue event
  updateGpxInfo(gpxInfo){
    this.gpxMaps[this.nowSelectMap].updateGpxInfo(gpxInfo);
  }

  updateGpxPoint(id, gpxPoint){
    // TODO
    this.gpxMaps.forEach((gpxMap) => {
      if(gpxMap.isHasPoint(id)){
        gpxMap.updatePoint(id, gpxPoint);
      }
    });
  }

  postGpxMaps(){
    let postData = [];
    this.gpxMaps.forEach((gpxMap) => {
      postData.push(gpxMap.toJson());
    });
    putGpxAPI(postData);
  }

  selectMap(index){
    this.nowSelectMap = index;
  }

  changeMode(mode){
    this.nowMode = mode;
  }

  // Dialog event
  showDialogMessage(message){

  }

  // Loading Component
  showLoading(){
  
  }

  hideLoading(){

  }
}