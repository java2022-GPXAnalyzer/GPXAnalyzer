import axios from 'axios';

import MockAdapter from 'axios-mock-adapter';

var mock = new MockAdapter(axios, { delayResponse: 100 });

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
export const putGpxAPI = (gpx) => gpxMapRequest.put('', gpx);
export const putGpxAPIForId = (id, gpx) => gpxMapRequest.put(id, gpx);

// MOCK DATA

const MOCkData = {
  maps: [
    {
      trkpoints: [
        {
          uuid: 'sdgs',
          lng: '120.178',
          lat: '23.566',
          ele: '5550',
          time: '2022-06-16T00:00:00Z',
        },
        {
          uuid: 'sdgs2',
          lng: '120.978',
          lat: '24.566',
          ele: '5550',
          time: '2022-06-16T00:00:05Z',
        },
        {
          uuid: 'sdgs3',
          lng: '121.578',
          lat: '24.5166',
          ele: '5550',
          time: '2022-06-16T00:00:10Z',
        },
        {
          uuid: 'sdgs4',
          lng: '121.348',
          lat: '23.5222',
          ele: '5550',
          time: '2022-06-16T00:00:15Z',
        },
        {
          uuid: 'sdgs5',
          lng: '121.588',
          lat: '22.5566',
          ele: '5550',
          time: '2022-06-16T00:00:20Z',
        },
      ],
      uuid: 'testMap',
      name: 'testMassss',
      startTime: '2022-06-16T00:00:00Z',
      endTime: '2022-06-16T00:00:20Z',
      creator: 'test',
      version: '1.0',
    },
    {
      trkpoints: [
        {
          uuid: 'sdaags',
          lng: '120.1278',
          lat: '22.566',
          ele: '5550',
          time: '2022-06-16T00:00:00Z',
        },
        {
          uuid: 'sdddgs2',
          lng: '121.978',
          lat: '23.5636',
          ele: '5550',
          time: '2022-06-16T00:00:05Z',
        },
      ],
      uuid: 'testMap2',
      name: 'testMap2',
      startTime: '2022-06-16T00:00:00Z',
      endTime: '2022-06-16T00:00:05Z',
      creator: 'test',
    },
  ],
};

mock.onGet("/gpxApi/idList/").reply(200, {
  status: 200,
  msg: '',
  result: ['testMap', 'testMap2']
});

mock.onGet("/gpxApi/gpx/testMap/gpxInfo").reply(200, {
  status: 200,
  msg: '',
  result: MOCkData.maps[0]
});

mock.onGet("/gpxApi/gpx/testMap/trackPoints").reply(200, {
  status: 200,
  msg: '',
  result: MOCkData.maps[0].trkpoints
});

mock.onGet("/gpxApi/gpx/testMap/wayPoints").reply(200, {
  status: 200,
  msg: '',
  result: null
});

mock.onGet("/gpxApi/gpx/testMap2/gpxInfo").reply(200, {
  status: 200,
  msg: '',
  result: MOCkData.maps[1]
});

mock.onGet("/gpxApi/gpx/testMap2/trackPoints").reply(200, {
  status: 200,
  msg: '',
  result: MOCkData.maps[1].trkpoints
});

mock.onGet("/gpxApi/gpx/testMap2/wayPoints").reply(200, {
  status: 200,
  msg: '',
  result: null
});

mock.onPut("/gpxApi/gpx/").reply(function(config){
  console.log(config.data);
  return [200, {}];
});

mock.onPost("/gpxApi/gpx/").reply(function(config){
  console.log('?', config.data);
  return [200, {msg: 'success'}];
});