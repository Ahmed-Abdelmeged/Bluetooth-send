## Bluetooth-send

It's a simple app with just two buttons that part of a big IOT project
you can customize the UI as you want this is just a simple example it's job
to send a command from the android device to the microcontroller you
work in we will show an simple example with arduino

## Library 
You can use [BluetoothMC](https://github.com/Ahmed-Abdelmeged/Android-BluetoothMCLibrary) library for easy 
bluetooth communication with micro controller 

## Feature

* Apply material design
* Easy to use
* The code well documented and easy to understand
* Easy to integrate at any exciting project

## Use cases
The main usage for this after you done your customization in he UI
is to control a microcontroller example of this control a car, Prostheses ..etc.
The options and ideas is a lot

## Getting started
* If you want to use the project in it's state and customize it as your needs

The connection to Bluetooth device is already handled
only you need to use a method `sendData` and call it when you want to
send a data or action to the microcontroller it takes a string as input parameter
in this case the data that will be send and it's preferred to be character and
that's it :D

You also may need to change the application name you can do it
form the strings file and put your awesome app name
`<string name="app_name">BluetoothSend</string>`

* If you want to integrate this functionality to exciting project follow along
with these steps

1- Add the bluetooth permissions in the `manifest`
```
<!-- Bluetooth Permissions -->
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

2- Also in the `manifest` make the screen Orientation **the app right now
does not  handle the screen Orientation in the runtime**

`android:screenOrientation="portrait"`
or
`android:screenOrientation="landscape"`

3- Don't forget to add the design support dependency in `build.gradle`
>`compile 'com.android.support:design:25.3.1'`

4- Put the required resources files
* in the drawable , layout  and strings you will find them in this example

5- Create a Bluetooth devices activity that will help to find and connect
to bluetooth device [BluetoothDevices.java](https://github.com/Ahmed-Abdelmeged/Bluetooth-send/blob/master/app/src/main/java/app/mego/bluetoothsend/BluetoothDevices.java)
[BluetoothDevices.xml](https://github.com/Ahmed-Abdelmeged/Bluetooth-send/blob/master/app/src/main/res/layout/activity_bluetooth_devices.xml)


 6- Add the Bluetooth device adapter that handle show and display the devices
 in the Bluetooth Devices activity
This the adapter code [BluetoothDeviceAdapter.java](https://github.com/Ahmed-Abdelmeged/Bluetooth-send/blob/master/app/src/main/java/app/mego/bluetoothsend/BluetoothDevicesAdapter.java)     
[BluetoothDeviceItem.xml](https://github.com/Ahmed-Abdelmeged/Bluetooth-send/blob/master/app/src/main/res/layout/item_device.xml)

7- Now the last step is to configure the activity that will handle sending and connecting to the microcontroller you can find the code for this logic in the [MainActivity.java](https://github.com/Ahmed-Abdelmeged/Bluetooth-send/blob/master/app/src/main/java/app/mego/bluetoothsend/MainActivity.java)  in `onResume()` you connect to the device and in
`onStop` you disconnect from the devices using the `Disconnect()`
method and the `ConnectBT ` AsyncTask to connect to the device
in background thread after the connection is success use the
`sendData` to communicate with the microcontroller

## Microcontroller

I use **Arduino** as simple example for test the code but it work for the other microcontrollers and this the Arduino code [BluetoothSendArduino.ino](https://github.com/Ahmed-Abdelmeged/Bluetooth-send/blob/master/BluetoothSendArduino/BluetoothSendArduino.ino)

![BluetoothDevices](http://imgur.com/SN7DG21.png)

![MainActivity](http://imgur.com/EWG6g59.png)

## License

Copyright (c) 2017 Ahmed-Abdelmeged.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
