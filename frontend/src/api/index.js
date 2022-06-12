import axios from 'axios'

const gpxMapRequest = axios.create({
  baseURL: 'http://localhost:3000/gpxApi/gpx/',
  headers: { 'Content-Type': 'application/json' },
});

const gpxsRequest = axios.create({
  baseURL: 'http://localhost:3000/gpxApi/idList/',
  headers: { 'Content-Type': 'application/json' },
});

export const getGpxList = () => gpxsRequest.get('/');

export const getGpx = (id) => gpxMapRequest.get(id);
export const getGpxInfo = (id) => gpxMapRequest.get(id + '/gpxInfo');
export const getGpxTrackPoints = (id) => gpxMapRequest.get(id + '/trackPoints');
export const getGpxWayPoints = (id) => gpxMapRequest.get(id + '/wayPoints');
