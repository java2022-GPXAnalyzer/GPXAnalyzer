import * as Cesium from 'cesium';
import { CesiumUtility } from '@/cesium/cesium.js';
import { eventManager } from '@/cesium/eventManager';

export class Handler{
  #handler;
  #viewer;
  
  constructor(viewer){
    this.emi = eventManager.getInstance();
    this.#viewer = viewer;
    this.#handler = new Cesium.ScreenSpaceEventHandler(viewer.scene.canvas);
  }

  initialize(){
    this.#handler.setInputAction(
      (e) => this.click(e), Cesium.ScreenSpaceEventType.LEFT_CLICK);
    this.#handler.setInputAction(
      (e) => this.leftDown(e), Cesium.ScreenSpaceEventType.LEFT_DOWN);
    this.#handler.setInputAction(
      (e) => this.leftUp(e), Cesium.ScreenSpaceEventType.LEFT_UP);
    this.#handler.setInputAction(
      (e) => this.mouseMove(e), Cesium.ScreenSpaceEventType.MOUSE_MOVE);
  }

  click(e){
    let pick = this.#viewer.scene.pick(e.position);
    let ray = this.#viewer.camera.getPickRay(e.position);
    let cartesian = this.#viewer.scene.globe.pick(ray, this.#viewer.scene);
    // console.log(CesiumUtility.convertToWGS(cartesian));
    if (Cesium.defined(pick)){
      var id = Cesium.defaultValue(pick.id, pick.primitive.id);
      if (id instanceof Cesium.Entity) {
        console.log(id._id)
      }
    }
  }

  leftDown(e){
    this.pointDraged = this.#viewer.scene.pick(e.position);
    this.leftDownFlag = true;
    if (this.pointDraged) {
      this.#viewer.scene.screenSpaceCameraController.enableRotate = false;
    } else {
      this.#viewer.scene.screenSpaceCameraController.enableRotate = true;
    }
  }

  leftUp(e){
    this.leftDownFlag = false;
    this.pointDraged = null;
    this.#viewer.scene.screenSpaceCameraController.enableInputs = true;
  }

  mouseMove(e){
    if (this.leftDownFlag === true && this.pointDraged != null) {
      let ray = this.#viewer.camera.getPickRay(e.endPosition);
      let cartesian = this.#viewer.scene.globe.pick(ray, this.#viewer.scene);
      this.pointDraged.id.position = new Cesium.CallbackProperty(function () {
        return cartesian;
      }, false);
      this.emi.updatePoint(this.pointDraged.id._id, CesiumUtility.convertToWGS(cartesian));
    }
  }
}