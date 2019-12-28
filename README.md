# Nearby-Application
Nearby application is an android app to view the nearest places from your location.

## Application Overview
Application consists of a single screen and it has two modes 
- **Single mode**: in which the application fetches nearby places only in the first run 
- **Realtime mode**: in which the application fetches nearby places if a user moved by 500 m from the location of last retrieved places. 

By default, realtime mode is triggered in the first launch of the application.

The application uses FoursquareAPI to fetch nearby locations.

You can choose the mode you desire by clicking on the **Floating Button**.

## Cofigurations
Before you start to run the code, please enter config file located in app/src/main/res/values 
and update client id and client secret values to be able to use foursquare API,
you can obtain credentials from: [Foursquare Auth](https://developer.foursquare.com/overview/auth)

## Desgin patterns overview
There are mainly 3 design patterns used in the architecture
- **MVP**
- **Repository**
- **Service Locator** (For dependency injection)

## Architecture overview
The application architecture consists of three layers
### 1- Network
The network layer is responsible for network calls to the server, it consists of
- **api** component contains the network HTTP client **(Retrofit)** and the server api gateway (**Requests**).

- **model** component contains objects representations of server responses.

- **repository** component is the interface between the network layer and the underlying layer.

- **exceptions** class that hold the network errors, translatable by the underlying layer.
### 2- Core
The core layer is the mediation layer between data layers (network, database, ...etc) and the android application it consists of
- **repository** component is the key component in core layer, Its mission is to get the data from any data source, convert it 
using the converter component to a unified model and acts as an interface to the underlying layer.

- **converter** component converts the models of the different data sources to a unified model.

- **model** component contains data classes that is used by the underlying layer.

- **ServiceLocator** class is responsible for providing dependencies for all the moduels in the application.

### 3- UI
The UI contains the android components (activities, fragments, ...etc) and the presenters which contain the logic.

## Dependencies
* **AndroidX**
* **Retrofit**
* **Gson**
* **RxAndroid, RxKotlin**
* **Google Play Services Location**
* **Glide**
