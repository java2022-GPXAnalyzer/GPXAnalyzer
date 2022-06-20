import * as Cesium from 'cesium';
import {
  getGpxInfoAPI,
  getGpxTrackPointsAPI,
  getGpxWayPointsAPI,
} from '@/api/index';

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

  static getGradientColor(speed, distance) {
    var ramp = document.createElement('canvas');
    ramp.width = 10;
    ramp.height = 1;
    var ctx = ramp.getContext('2d');
    var grd = ctx.createLinearGradient(0, 0, 10, 1);
    for (let i = 0; i < speed.length; i++) {
      grd.addColorStop(
        distance[i],
        `rgb(255, ${speed[i] * 255}, ${speed[i] * 255})`
      );
    }
    ctx.fillStyle = grd;
    ctx.fillRect(0, 0, 10, 1);
    return ramp;
  }

  static getGeoHeight(viewer, lng, lat) {
    var cartographic = Cesium.Cartographic.fromDegrees(lng, lat);
    return viewer.scene.globe.getHeight(cartographic);
    // return Cesium.Cartographic.fromDegrees(lng, lat).height;
  }

  static convertPositionFromWGS(position) {
    return Cesium.Cartesian3.fromDegrees(
      position.lng,
      position.lat,
      position.ele
    );
  }

  static _uuid() {
    var d = Date.now();
    if (
      typeof performance !== 'undefined' &&
      typeof performance.now === 'function'
    ) {
      d += performance.now(); //use high-precision timer if available
    }
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(
      /[xy]/g,
      function (c) {
        var r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c === 'x' ? r : (r & 0x3) | 0x8).toString(16);
      }
    );
  }
}

export class WayPoint {
  viewer;

  constructor(viewer, point) {
    this.viewer = viewer;
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

  getId() {
    return this.uuid;
  }

  getTime() {
    return this.time;
  }

  getPosition() {
    return this.position;
  }

  drawPoint() {
    this.entity = this.viewer.entities.add(this.entityInfo);
  }

  hover() {
    this.entity.point.color = Cesium.Color.BLUE;
  }

  unhover() {
    this.entity.point.color = Cesium.Color.RED;
  }

  toggleDisplay() {
    this.entity.show = !this.entity.show;
  }

  addProperty(property) {
    property.addSample(this.time, this.position);
  }

  _updatePosition(position) {
    this.lat = position.lat;
    this.lng = position.lng;
    this.ele = position.ele;
    this.position = Cesium.Cartesian3.fromDegrees(this.lng, this.lat, this.ele);
    return this.position;
  }

  update(pointData) {
    let cartesian = this._updatePosition(pointData);
    this.entity.position = new Cesium.CallbackProperty(() => {
      return cartesian;
    }, false);
  }

  destroy() {
    this.viewer.entities.remove(this.entity);
  }

  toJson() {
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

  constructor(viewer, id, index) {
    this.viewer = viewer;
    this.isDrawed = false;
    this.isChanged = false;
    this.wayPoints = [];
    this.trkPoints = [];
    this.uuid = id;
    this.index = index;
    this.startTime = null;
    this.endTime = null;
    this.isSpeedDistribution = false;
    this.isHover = false;
    this.gradientColor = null;
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
      this.trkPoints.push(new WayPoint(this.viewer, point));
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
      this.wayPoints.push(new WayPoint(this.viewer, point));
    });
  }

  getStartTimeClone() {
    return this.startTime.clone();
  }

  getEndTimeClone() {
    return this.endTime.clone();
  }

  getId() {
    return this.uuid;
  }

  getPoint(id) {
    return this.findPoints(id);
  }

  getEntityInfo() {
    return this.entityInfo;
  }

  findPoints(id) {
    return (
      this.trkPoints.find((point) => {
        return point.getId() === id;
      }) ||
      this.wayPoints.find((point) => {
        return point.getId() === id;
      })
    );
  }

  addPoint(item, pointData) {
    pointData = CesiumUtility.convertToWGS(pointData);
    pointData.time = new Date().toISOString();
    pointData.uuid = CesiumUtility._uuid();
    var point = new WayPoint(this.viewer, pointData);
    this[item].push(point);
    if (item == 'trkPoints') {
      this.endTime = Cesium.JulianDate.fromIso8601(pointData.time);
      this.activeTime();
      this.reDrawLine();
    }
    return point;
  }

  updatePoint(id, pointData) {
    let point = this.findPoints(id);
    if (point) point.update(pointData);
  }

  removePoint(pointId) {
    let point = this.findPoints(pointId);
    if (point) {
      let wayPointFlag = 0;
      this.wayPoints = this.wayPoints.filter((point) => {
        let id = point.getId();
        if (id === pointId) {
          point.destroy();
          wayPointFlag = 1;
          return false;
        }
        return id !== pointId;
      });

      if (wayPointFlag) return;

      this.trkPoints = this.trkPoints.filter((point) => {
        let id = point.getId();
        if (id === pointId) {
          point.destroy();
          return false;
        }
        return id !== pointId;
      });

      this.reDrawLine();
      this.startTime = this.trkPoints[0].getTime();
      this.endTime = this.trkPoints[this.trkPoints.length - 1].getTime();
      this.activeTime();
    }
  }

  updateGpxInfo(gpxInfo) {
    this.name = gpxInfo.name;
    this.creator = gpxInfo.creator;
    this.version = gpxInfo.version;
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
      // orientation: new Cesium.VelocityOrientationProperty(property),
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

    this.entity = this.viewer.entities.add(this.entityInfo);
    this.isDrawed = true;
  }

  _drawSpeedDistribution() {
    if(this.gradientColor === null) {
      // TODO
      this.gradientColor = CesiumUtility.getGradientColor(
        [0, 0.25, 0.5, 0.75, 1],
        [0, 0.75, 1, 0.2, 0.2]
      );
    }
    this.entity.path.material = this.gradientColor;
  }

  toggleSpeedDistribution() {
    if (this.isChanged){
      return false;
    }
    if (this.isSpeedDistribution) {
      this.entity.path.material = CesiumUtility.getColor(this.index);
      this.isSpeedDistribution = false;
    } else {
      this._drawSpeedDistribution();
      this.isSpeedDistribution = true;
    }
    return true;
  }

  toggleHover() {
    if (!this.isSpeedDistribution && !this.isHover) {
      let color = CesiumUtility.getColor(this.index);
      let flag = 1;
      let alp = 1;
      this.entity.path.material = new Cesium.ImageMaterialProperty({
        transparent: true,
        color: new Cesium.CallbackProperty(function () {
          if (flag) {
            alp -= 0.02;
          } else {
            alp += 0.02;
          }
          if (alp <= 0.1) {
            flag = 0;
          } else if (alp >= 1) {
            flag = 1;
          }
          return color.withAlpha(alp);
        }, false),
      });
      this.isHover = true;
    }
    else if(!this.isSpeedDistribution && this.isHover) {
      this.entity.path.material = CesiumUtility.getColor(this.index);
      this.isHover = false;
    }
  }

  toggleDisplay() {
    this.wayPoints.forEach((point) => {
      point.toggleDisplay();
    });
    this.trkPoints.forEach((point) => {
      point.toggleDisplay();
    });
    this.entity.show = !this.entity.show;
  }

  reDrawLine() {
    this.viewer.entities.remove(this.entity);
    this.isChanged = true;
    this.gradientColor = null;
    this.drawLine();
  }

  activeTime() {
    this.viewer.clock.startTime = this.getStartTimeClone();
    this.viewer.clock.stopTime = this.getEndTimeClone();
    this.viewer.clock.currentTime = this.getStartTimeClone();
    // this.viewer.timeline.zoomTo(this.startTime, this.endTime);
  }

  isActive() {
    return this.isDrawed;
  }

  isHasPoint(id) {
    return (
      this.trkPoints.find((point) => {
        return point.getId() === id;
      }) ||
      this.wayPoints.find((point) => {
        return point.getId() === id;
      })
    );
  }

  toJson() {
    return {
      gpxInfo: {
        uuid: this.uuid,
        name: this.name,
        creator: this.creator,
        version: this.version,
        startTime: this.startTime.toString(),
        endTime: this.endTime.toString(),
      },
      trackPoints: [
        ...this.trkPoints.map((point) => {
          return point.toJson();
        }),
      ],
      wayPoints: [
        ...this.wayPoints.map((point) => {
          return point.toJson();
        }),
      ],
    };
  }

  // Test
  test() {
    return this.uuid;
  }
}
