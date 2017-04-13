# ServerScalingService
The ServerScalingService controls the deployment of more servers of specific types, to ensure there is always enough capacity. Right now it communicates only with the kubernetes backend but it may start to communicate with Openshift as well to deploy more nodes.

## Endpoints

### /games/register [POST]:
#### Updates a game type

**Request**:

The name should must the gameId!

```json
{
"name": "mg_gw",
"image": "exorath/mg_gw:0.0.1",
"terminationGracePeriodSeconds": 1000,
"env": {
  "key1": "value1",
  "key2": "value2"
},
"labels": {}
}
```

**Response**:
```json
{
"success": true
}
```


### /bungee [POST]:
#### Updates the bungee spec
#### NOTICE: THE BUNGEE ENDPOINT IS CURRENTLY NOT IN USE!!!
**Request**:
```json
{
"image": "exorath/bungee:0.0.1",
"terminationGracePeriodSeconds": 1000,
"env": {
  "key1": "value1",
  "key2": "value2"
},
"labels": {}
}
```

**Response**:
```json
{
"success": true
}
```


##Environment
| Name | Value |
| --------- | --- |
| MONGO_URI | {mongo_uri} |
| DB_NAME | {db name to store data} |
| CONNECTOR_SERVICE_ADDRESS | {ConnectorService ip:port} |
| KUBERNETES_ADDRESS | {Kubernetes url} |