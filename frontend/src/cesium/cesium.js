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

  update(pointData){
    cartesian = pointData.cartesian;
    this.entity.id.position = new Cesium.CallbackProperty(() => {
      return cartesian;
    }, false);
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

  findPoints(id) {
    return this.#viewer.entities.getById(id);
  }

  addPoint(items, pointData) {
    var point = new WayPoint(this.#viewer, pointData);
    this[items].push(point);
    return point;
  }

  updatePoint(id, pointData) {    
    let point = this.findPoints(id);
    if(point) point.update(pointData);
  }

  drawLine() {
    let property = new Cesium.SampledPositionProperty();
    this.trkPoints.forEach((point) => {
      point.addProperty(property);
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

  // Test
  test() {
    return this.uuid;
  }
}
