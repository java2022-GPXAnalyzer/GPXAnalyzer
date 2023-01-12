export class eventManager{
  static getInstance() {
    if (!eventManager.instance) {
      eventManager.instance = new eventManager();
    }
    return eventManager.instance;
  }

  // on(eventName, callback) {
  //   if (!this.eventMap.has(eventName)) {
  //     this.eventMap.set(eventName, []);
  //   }
  //   this.eventMap.get(eventName).push(callback);
  // }

  // Keyboard Event

  keyboardEvent = () => import('@/eventManager/keyboardEvent.js');


  // Test

  test = async () => await import('@/eventManager/test.js');
}