#!/bin/sh
javac -d "./bin/" -cp ".:./src/opencv-300.jar:./bin/" ./src/ImageProcessor.java
javac -d "./bin/" -cp ".:./src/opencv-300.jar:./bin/" ./src/App.java
