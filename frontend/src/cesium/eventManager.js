import { getGpxListAPI } from '@/api/index';
import { GpxMap } from '@/cesium/cesium';
import { Handler } from '@/cesium/handler';

export class eventManager{
  gpxMaps = [];

  static getInstance(){
    if(!eventManager.instance){
      eventManager.instance = new eventManager();
    }
    return eventManager.instance;
  }

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

  setGpxMaps(gpxMaps){
    this.gpxMaps = gpxMaps;
  }
}