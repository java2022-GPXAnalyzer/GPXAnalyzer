import * as Cesium from 'cesium';
import { getGpxInfo } from '@/api/index';

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
      Cesium.Color.GREEN,
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
}

export class WayPoint {
  #viewer;

  constructor(viewer, point) {
    this.#viewer = viewer;
    this.lng = parseFloat(point.lng);
    this.lat = parseFloat(point.lat);
    this.ele = parseFloat(point.ele);
    this.position = Cesium.Cartesian3.fromDegrees(this.lng, this.lat, this.ele);
    this.time = Cesium.JulianDate.fromIso8601(point.time);
    this.id = point.id;
    this.name = point.name || '';
    this.entity = new Cesium.Entity({
      name: this.name,
      id: this.id,
      position: this.position,
      point: {
        pixelSize: 5,
        color: Cesium.Color.RED,
        outlineColor: Cesium.Color.WHITE,
        outlineWidth: 2,
      },
    });
    this.drawPoint();
  }

  drawPoint() {
    this.#viewer.entities.add(entity);
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
    return this.id;
  }
}

export class GpxMap {
  #viewer;

  constructor(viewer, id) {
    this.#viewer = viewer;
    this.wayPoints = [];
    this.trkPoints = [];
    this.id = id;
    this.startTime = null;
    this.endTime = null;
    // initize();
  }

  initize() {
    this.loadGpxInfo();
    this.loadTrkPoints();
    this.loadWayPoints();
  }

  async loadGpxInfo() {
    var info = await getGpxInfo(this.id);
    info = info.result;
    this.startTime = info.startTime;
    this.endTime = info.endTime;
    this.creator = info.creator;
    this.version = info.version;
    this.name = info.name || '';
  }

  async loadTrkPoints() {
    var trkPoints = await getGpxTrackPoints(this.id);
    trkPoints = trkPoints.result;
    trkPoints.forEach((point) => {
      this.trkPoints.push(new WayPoint(this.#viewer, point));
    });
  }

  async loadWayPoints() {
    var wayPoints = await getGpxWayPoints(this.id);
    wayPoints = wayPoints.result;
    wayPoints.forEach((point) => {
      this.wayPoints.push(new WayPoint(this.#viewer, point));
    });
  }

  findPoints(items, id) {
    for (let i = 0; i < this[items].length; i++) {
      if (this[items][i].getId() === id) {
        return this[items][i];
      }
    }
    return null;
  }

  addPoint(pointData) {
    var point = new WayPoint(this.#viewer, pointData);
    this.wayPoints.push(point);
    return point;
  }

  // Test
  test() {
    console.log(this);
    console.log(this.id);
    console.log(this['trkPoints']);
  }
}
