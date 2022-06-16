import * as Cesium from 'cesium';
import { getGpxInfoAPI, getGpxTrackPointsAPI, getGpxWayPointsAPI } from '@/api/index';

export class CesiumUtility {
  static convertToWGS(position) {
    var wgs84 = Cesium.Cartographic.fromCartesian(position);
    var lat = Cesium.Math.toDegrees(wgs84.latitude);
    var lng = Cesium.Math.toDegrees(wgs84.longitude);
    var ele = wgs84.height;
    return {
      lat: lat,
      lng: lng,
      ele: ele,
    };
  }

  static getColor(index) {
    var colors = [
      // Cesium.Color.RED,
      // Cesium.Color.GREEN,
      Cesium.Color.BLUE,
      Cesium.Color.YELLOW,
      Cesium.Color.CYAN,
      Cesium.Color.MAGENTA,
      Cesium.Color.ORANGE,
      Cesium.Color.WHITE,
      Cesium.Color.BLACK,
    ];
    return colors[index % colors.length];
  }

  static getGeoHeight(viewer, lng, lat){
    var cartographic = Cesium.Cartographic.fromDegrees(lng, lat);
    return viewer.scene.globe.getHeight(cartographic);
    // return Cesium.Cartographic.fromDegrees(lng, lat).height;
  }

  static convertPositionFromWGS(position) {
    return Cesium.Cartesian3.fromDegrees(position.lng, position.lat, position.ele);
  }

  static _uuid() {
    var d = Date.now();
    if (typeof performance !== 'undefined' && typeof performance.now === 'function'){
      d += performance.now(); //use high-precision timer if available
    }
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
      var r = (d + Math.random() * 16) % 16 | 0;
      d = Math.floor(d / 16);
        return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
  }
}

export class WayPoint {
  #viewer;

  constructor(viewer, point) {
    this.#viewer = viewer;
    this.lng = parseFloat(point.lng);
    this.lat = parseFloat(point.lat);
    // TODO ele
    this.ele = parseFloat(point.ele);
    this.time = Cesium.JulianDate.fromIso8601(point.time);
    this.uuid = point.uuid;
    this.name = point.name || '';
    this.position = Cesium.Cartesian3.fromDegrees(this.lng, this.lat, this.ele);
    this.entityInfo = {
      name: this.name,
      id: this.uuid,
      position: this.position,
      point: {
        pixelSize: 8,
        color: Cesium.Color.RED,
        outlineColor: Cesium.Color.WHITE,
        outlineWidth: 1,
      },
    };
    this.drawPoint();
  }

  drawPoint() {
    this.entity = this.#viewer.entities.add(this.entityInfo);
  }

  hover() {
    this.entity.point.color = Cesium.Color.BLUE;
  }

  unhover() {
    this.entity.point.color = Cesium.Color.RED;
  }

  addProperty(property) {
    property.addSample(this.time, this.position);
  }

  getId() {
    return this.uuid;
  }

  _updatePosition(position){
    this.lat = position.lat;
    this.lng = position.lng;
    this.ele = position.ele;
    this.position = Cesium.Cartesian3.fromDegrees(this.lng, this.lat, this.ele);
    return this.position;
  }

  update(pointData){
    let cartesian = this._updatePosition(pointData);
    this.entity.position = new Cesium.CallbackProperty(() => {
      return cartesian;
    }, false);
  }

  toJson(){
    return {
      uuid: this.uuid,
      lng: this.lng,
      lat: this.lat,
      ele: this.ele,
      time: Cesium.JulianDate.toIso8601(this.time),
      name: this.name,
    };
  }
}

export class GpxMap {
  #viewer;
  #isDrawed;

  constructor(viewer, id, index) {
    this.#viewer = viewer;
    this.#isDrawed = false;
    this.wayPoints = [];
    this.trkPoints = [];
    this.uuid = id;
    this.index = index;
    this.startTime = null;
    this.endTime = null;
  }

  async initialize() {
    this.loadGpxInfo();
    this.loadWayPoints();
    this.loadTrkPoints();
  }

  async loadGpxInfo() {
    var info = await getGpxInfoAPI(this.uuid);
    info = info.data.result;
    this.startTime = Cesium.JulianDate.fromIso8601(info.startTime);
    this.endTime = Cesium.JulianDate.fromIso8601(info.endTime);
    this.creator = info.creator;
    this.version = info.version;
    this.name = info.name || '';
    this.activeTime();
  }

  async loadTrkPoints() {
    var trkPoints = await getGpxTrackPointsAPI(this.uuid);
    trkPoints = trkPoints.data.result;
    if (trkPoints === null || trkPoints.length === 0) {
      return;
    }
    trkPoints.forEach((point) => {
      this.trkPoints.push(new WayPoint(this.#viewer, point));
    });
    this.drawLine();
  }

  async loadWayPoints() {
    var wayPoints = await getGpxWayPointsAPI(this.uuid);
    wayPoints = wayPoints.data.result;
    if (wayPoints === null || wayPoints.length === 0) {
      return;
    }
    wayPoints.forEach((point) => {
      this.wayPoints.push(new WayPoint(this.#viewer, point));
    });
  }

  getStartTimeClone() {
    return this.startTime.clone();
  }

  getEndTimeClone() {
    return this.endTime.clone();
  }

  getId(){
    return this.uuid;
  }

  findPoints(id) {
    // return this.#viewer.entities.getById(id);
    return this.trkPoints.find((point) => {
      return point.getId() === id;
    }) || this.wayPoints.find((point) => {
      return point.getId() === id;
    });
  }

  addPoint(item, pointData) {
    pointData = CesiumUtility.convertToWGS(pointData);
    pointData.time = (new Date()).toISOString();
    pointData.uuid = CesiumUtility._uuid();
    var point = new WayPoint(this.#viewer, pointData);
    this[item].push(point);
    if(item == "trkPoints") {
      this.endTime = Cesium.JulianDate.fromIso8601(pointData.time);
      this.activeTime();
      this.reDrawLine();
    }
    return point;
  }

  updatePoint(id, pointData) {    
    let point = this.findPoints(id);
    if(point) point.update(pointData);
  }

  updateGpxInfo(gpxInfo){
    this.name = gpxInfo.name;
    this.creator = gpxInfo.creator;
    this.version = gpxInfo.version;
  }

  drawLine() {
    let property = new Cesium.SampledPositionProperty();
    this.trkPoints.forEach((point) => {
      point.addProperty(property);
    });

    let test =  new Cesium.TimeInterval({
      start: this.startTime,
      stop: this.endTime,
    });

    this.entityInfo = {
      availability: new Cesium.TimeIntervalCollection([
        new Cesium.TimeInterval({
          start: this.startTime,
          stop: this.endTime,
        }),
      ]),
      position: property,
      orientation: new Cesium.VelocityOrientationProperty(property),
      path: {
        resolution: 1,
        width: 2,
        material: CesiumUtility.getColor(this.index),
      },
      point: {
        color: CesiumUtility.getColor(this.index),
        pixelSize: 15,
      },
    };

    this.entity = this.#viewer.entities.add(this.entityInfo);
    this.#isDrawed = true;
  }

  reDrawLine() {
    this.#viewer.entities.remove(this.entity);
    this.drawLine();
  }

  getEntityInfo() {
    return this.entityInfo;
  }

  activeTime(){
    this.#viewer.clock.startTime = this.getStartTimeClone();
    this.#viewer.clock.stopTime = this.getEndTimeClone();
    this.#viewer.clock.currentTime = this.getStartTimeClone();
    // this.#viewer.timeline.zoomTo(this.startTime, this.endTime);
  }

  isActive(){
    return this.#isDrawed;
  }

  isHasPoint(id){
    return this.trkPoints.find((point) => {
      return point.getId() === id;
    }) || this.wayPoints.find((point) => {
      return point.getId() === id;
    });
  }

  toJson(){
    return {
      gpxInfo:{
        uuid: this.uuid,
        name: this.name,
        creator: this.creator,
        version: this.version,
        startTime: this.startTime.toString(),
        endTime: this.endTime.toString(),
      },
      trackPoints:[
        ...this.trkPoints.map((point) => {
          return point.toJson();
        })
      ],
      wayPoints:[
        ...this.wayPoints.map((point) => {
          return point.toJson();
        })
      ]
    }
  }

  // Test
  test() {
    return this.uuid;
  }
}
