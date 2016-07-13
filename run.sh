#!/bin/sh
java -Djava.library.path=../opencv/build/lib// -cp ".:./src/opencv-300.jar:./bin" App
