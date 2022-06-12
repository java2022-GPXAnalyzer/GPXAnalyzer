<template>
  <button @click="track" class="m-3">track</button>
  <button @click="begin" class="m-3">begin</button>
  <button @click="stop" class="m-3">stop</button>
  <button @click="reset" class="m-3">reset</button>
  <button @click="add" class="m-3">add</button>
  <button @click="test" class="m-3">test</button>
  <p>{{ state.mode }}</p>
  <div class="container">
    <div id="cesiumContainer"></div>
  </div>
</template>

<script setup>
import * as Cesium from 'cesium';
import { onMounted, reactive } from 'vue';

import { GpxMap } from '@/cesium/cesium';

const state = reactive({
  name: '',
  mode: 0,
});

// Cesium Setting
Cesium.Ion.defaultAccessToken =
  'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJjNThmYjdjNi04MWU0LTQ3NDktOGVlMi05ZTVkNzExMWM0NGYiLCJpZCI6OTcxNDUsImlhdCI6MTY1NDkzMDE4NH0.1UyvbLY84Rmpj0IPPBHhyjEtqddTyz0Ovf0IhoKK0zA';
var viewer;
var handler;

const utility = {
  convertWGSfromDKR3: function (position) {
    var wgs84 = Cesium.Cartographic.fromCartesian(position);
    var lat = Cesium.Math.toDegrees(wgs84.latitude);
    var lng = Cesium.Math.toDegrees(wgs84.longitude);
    var alt = wgs84.height;
    return {
      lat: lat,
      lng: lng,
      alt: alt,
    };
  },
};

const fakeData = {
  points: [
    {
      uuid: 'sdgs',
      lng: '120.978',
      lat: '23.566',
      alt: '5550',
      time: '2020-01-01T00:00:00Z',
    },
    {
      uuid: 'sdgs2',
      lng: '121.978',
      lat: '24.566',
      alt: '5550',
      time: '2020-01-01T00:01:05Z',
    },
    {
      uuid: 'sdgs3',
      lng: '122.978',
      lat: '24.5166',
      alt: '5550',
      time: '2020-01-01T00:02:10Z',
    },
  ],
  id: 'sdgsMAP',
  name: 'sdgsMAP',
  startTime: '2020-01-01T00:00:00Z',
  endTime: '2020-01-01T00:02:10Z',
};

var mapsDataInfo = [];
var mapsData = [];
var startTime;
var stopTime;

onMounted(() => {
  viewer = new Cesium.Viewer('cesiumContainer', {
    terrainProvider: Cesium.createWorldTerrain(),
    geocoder: false, //Hide search
    homeButton: false, //Hide back home
    sceneModePicker: false, //Hide view mode
    baseLayerPicker: false, //Hide layer section
    navigationHelpButton: false, //Hide help
    animation: false, //Hide animation controller
    timeline: false, //Hide timeline controller
    fullscreenButton: false, //Hide fullscreen button
    selectionIndicator: false, //Hide selection indicator
    infoBox: false, //Hide info box
  });
  handler = new Cesium.ScreenSpaceEventHandler(viewer.scene.canvas);

  // Cesium version hidden
  viewer._cesiumWidget._creditContainer.style.display = 'none';
  viewer.forceResize();

  // Set Initial View in Taiwan
  viewer.camera.setView({
    destination: Cesium.Cartesian3.fromDegrees(121.0, 23.5, 550000.0),
  });

  // Handler
  handler.setInputAction(function (e) {
    var pick = viewer.scene.pick(e.position);
    var ray = viewer.camera.getPickRay(e.position);
    var position = viewer.scene.globe.pick(ray, viewer.scene);
    // console.log(utility.convertWGSfromDKR3(position))
    // if (Cesium.defined(pick)){
    //   var id = Cesium.defaultValue(pick.id, pick.primitive.id);
    //   if (id instanceof Cesium.Entity) {
    //     console.log(id._id)
    //     state.name = id._id
    //   }
    // }
    if (state.mode >= 1) {
      addGPXPoint(0, utility.convertWGSfromDKR3(position));
    }
  }, Cesium.ScreenSpaceEventType.LEFT_CLICK);

  var leftDownFlag = false;
  var pointDraged = null;
  handler.setInputAction(function (movement) {
    pointDraged = viewer.scene.pick(movement.position);
    leftDownFlag = true;
    if (pointDraged) {
      viewer.scene.screenSpaceCameraController.enableRotate = false;
    } else {
      viewer.scene.screenSpaceCameraController.enableRotate = true;
    }
  }, Cesium.ScreenSpaceEventType.LEFT_DOWN);

  // Release plane on mouse up
  handler.setInputAction(function () {
    leftDownFlag = false;
    pointDraged = null;
    viewer.scene.screenSpaceCameraController.enableInputs = true;
  }, Cesium.ScreenSpaceEventType.LEFT_UP);

  // Update plane on mouse move
  handler.setInputAction(function (movement) {
    if (leftDownFlag === true && pointDraged != null) {
      let ray = viewer.camera.getPickRay(movement.endPosition);
      let cartesian = viewer.scene.globe.pick(ray, viewer.scene);
      pointDraged.id.position = new Cesium.CallbackProperty(function () {
        return cartesian;
      }, false);
    }
  }, Cesium.ScreenSpaceEventType.MOUSE_MOVE);

  // test
  // addGPXFromFile();
  addGPXFromJson(fakeData);

  // Test

  // viewer.entities.add( {
  //   id: 'test',
  //   name : 'Citizens Bank Park',
  //   position : Cesium.Cartesian3.fromDegrees(121.0, 23),
  //   point : { //点
  //     pixelSize : 2,
  //     color : Cesium.Color.RED,
  //     outlineColor : Cesium.Color.WHITE,
  //     outlineWidth : 1
  //   },
  // });

  viewer.clock.startTime = startTime.clone();
  viewer.clock.stopTime = stopTime.clone();
  viewer.clock.currentTime = startTime.clone();
  viewer.clock.clockRange = Cesium.ClockRange.LOOP_STOP;
  // viewer.timeline.zoomTo(startTime, stopTime);

  viewer.clock.shouldAnimate = false;
});

function addGPXFromJson(data) {
  // startTime = Cesium.JulianDate.fromIso8601(data.startTime);
  // stopTime = Cesium.JulianDate.fromIso8601(data.endTime);
  // let pointArray = [];

  // let property = new Cesium.SampledPositionProperty();
  // data.points.forEach(function(point){
  //   let lng = parseInt(point.lng);
  //   let lat = parseInt(point.lat);
  //   let alt = parseInt(point.alt);
  //   let position = Cesium.Cartesian3.fromDegrees(lng, lat, alt)
  //   let time = Cesium.JulianDate.fromIso8601(point.time);
  //   property.addSample(time, position);
  //   pointArray.push(position);
  //   viewer.entities.add({
  //     position: position,
  //     point: {
  //       pixelSize: 10,
  //       color: Cesium.Color.RED,
  //       outlineColor: Cesium.Color.WHITE,
  //       outlineWidth: 2
  //     }
  //   })
  // });

  // let mapInfo = {
  //   name: data.name,
  //   id: data.id,
  //   polyline: {
  //     positions: pointArray,
  //     width: 2,
  //     material: Cesium.Color.BLUE,
  //   },
  // };

  let mapInfoTmp = {
    availability: new Cesium.TimeIntervalCollection([
      new Cesium.TimeInterval({
        start: startTime,
        stop: stopTime,
      }),
    ]),
    position: property,
    // orientation: new Cesium.VelocityOrientationProperty(property),
    path: {
      width: 2,
      material: Cesium.Color.BLUE,
      resolution: 10,
    },
    point: {
      color: Cesium.Color.BLUE,
      pixelSize: 50,
    },
  };
  // mapsDataInfo.push(mapInfo);

  // let map = viewer.entities.add(mapInfo);
  let t = viewer.entities.add(mapInfoTmp);
  // console.log(t)
  // viewer.trackedEntity = t;
  // mapsData.push(map);
}

function addGPXFromFile() {
  // Add GPX
  const gpx = Cesium.GpxDataSource.load('test.gpx');
  console.log(gpx);
  viewer.dataSources.add(gpx, {
    clampToGround: true,
  });
}

function _notIn(positions, point) {
  for (let i = 0; i < positions.length; i++) {
    if (positions[i].equals(point)) {
      return false;
    }
  }
  return true;
}

function _addGPXPoint(map, mapInfo, pointData) {
  let lng = parseFloat(pointData.lng ? pointData.lng : pointData.x);
  let lat = parseFloat(pointData.lat ? pointData.lat : pointData.y);
  let alt = parseFloat(pointData.alt ? pointData.alt : 0);
  let point = Cesium.Cartesian3.fromDegrees(lng, lat, alt);
  // let positions = mapInfo.polyline.positions;
  // if(_notIn(positions, point)){
  //   mapInfo.polyline.positions.push(point);
  //   map.polyline.positions = positions;
  // }
  viewer.entities.add({
    position: point,
    point: {
      pixelSize: 10,
      color: Cesium.Color.RED,
      outlineColor: Cesium.Color.WHITE,
      outlineWidth: 2,
    },
  });
  if (state.mode === 1) state.mode = 0;
}

function addGPXPoint(id, pointData) {
  let map = mapsData[id];
  let mapInfo = mapsDataInfo[id];
  _addGPXPoint(map, mapInfo, pointData);
}

// TEST
function track() {
  // Track
  viewer.trackedEntity = undefined;
  console.log(viewer.scene.screenSpaceCameraController.enableInputs);
  viewer.scene.screenSpaceCameraController.enableInputs = true;
  // addGPXPoint(0, {
  //   lng: '123.978',
  //   lat: '24.566',
  //   alt: '5550',
  // });
  // let tmp = mapsData[0];
  // tmp.polyline.material = Cesium.Color.RED;
}

function begin() {
  viewer.clock.shouldAnimate = true;
}

function stop() {
  viewer.clock.shouldAnimate = false;
}

function reset() {
  viewer.clock.currentTime = startTime.clone();
}

function add() {
  // console.log()
  if (state.mode === 0) {
    state.mode = 1;
    return;
  }
  if (state.mode === 1) {
    state.mode = 2;
    return;
  }
  state.mode = 0;
}

function test() {
  // var s = new GpxMap(viewer, "1");
  // s.test();
}
</script>

<style>
.container {
  width: 100%;
  height: calc(100vh - 60px);
  margin: 20px auto 0;
}
#cesiumContainer {
  width: 80%;
  height: 100%;
  margin: auto;
  padding: 0;
  overflow: hidden;
}
</style>
