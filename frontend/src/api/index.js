import axios from 'axios';

import MockAdapter from 'axios-mock-adapter';

var mock = new MockAdapter(axios);

const gpxsRequest = axios.create({
  baseURL: '/gpxApi/idList/',
  headers: { 'Content-Type': 'application/json' },
});

const gpxMapRequest = axios.create({
  baseURL: '/gpxApi/gpx/',
  headers: { 'Content-Type': 'application/json' },
});


export const getGpxListAPI = () => gpxsRequest.get();

export const getGpxAPI = (id) => gpxMapRequest.get(id);
export const getGpxInfoAPI = (id) => gpxMapRequest.get(id + '/gpxInfo');
export const getGpxTrackPointsAPI = (id) => gpxMapRequest.get(id + '/trackPoints');
export const getGpxWayPointsAPI = (id) => gpxMapRequest.get(id + '/wayPoints');

export const postGpxFilePathAPI = (filepaths) => gpxMapRequest.post('', filepaths);
export const putGpxAPI = (gpx) => gpxMapRequest.put(gpx);
export const putGpxAPIForId = (id, gpx) => gpxMapRequest.put(id, gpx);

// MOCK DATA

const MOCkData = {
  points: [
    {
      uuid: 'sdgs',
      lng: '120.178',
      lat: '23.566',
      ele: '5550',
      time: '2021-06-16T00:00:00Z',
    },
    {
      uuid: 'sdgs2',
      lng: '120.978',
      lat: '24.566',
      ele: '5550',
      time: '2021-06-16T00:00:05Z',
    },
    {
      uuid: 'sdgs3',
      lng: '121.578',
      lat: '24.5166',
      ele: '5550',
      time: '2021-06-16T00:00:10Z',
    },
  ],
  uuid: 'testMap',
  name: 'testMap',
  startTime: '2021-06-16T00:00:00Z',
  endTime: '2021-06-16T00:00:10Z',
  creator: 'test',
  version: '1.0',
};

mock.onGet("/gpxApi/idList/").reply(200, {
  status: 200,
  msg: '',
  result: ['testMap']
});

mock.onGet("/gpxApi/gpx/testMap/gpxInfo").reply(200, {
  status: 200,
  msg: '',
  result: MOCkData
});

mock.onGet("/gpxApi/gpx/testMap/trackPoints").reply(200, {
  status: 200,
  msg: '',
  result: MOCkData.points
});

mock.onGet("/gpxApi/gpx/testMap/wayPoints").reply(200, {
  status: 200,
  msg: '',
  result: null
});