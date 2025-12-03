# Information
By Weikai Chen \
Email: weikai.c@gmail.com \
Github: https://github.com/wkpc/Minesweeper

This program runs Minesweeper.

## What it does
This program is my attempt at recreating the classic game Minesweeper. It plays and follows the same 
rules as classic minesweeper. The goal of the game is to clear all the tiles without hitting any mines.

## How to run it
To be able to run this game, download the "out" folder and unzip it. Make sure to keep all the contents of the folder 
together in the same directory. The other folders in the repository are not necessary to run the game. This program uses
the external JavaFX library. This is included in the "out" folder as "javafx-sdk-25.0.1", if you already have JavaFx 
installed on your computer, you can choose to skip this file. Note: it is not normally possible to download specific files from 
GitHub without extra workarounds, so you may have to download the entire project files to get the out folder.

To launch the game, simply launch the "Minesweeper.bat" file, by either double-clicking the file or opening Command 
Prompt and typing in the file path.

If you already had JavaFx installed on your computer and chose not to download the "javafx-sdk-25.0.1" file, you must
first open "Minesweeper.bat" in a text editor of your choice, and replace ".\javafx-sdk-25.0.1\lib" with the file URL of
the "lib" folder inside the JavaFX files on your computer. This program was designed with version 25.0.1 of JavaFX and
may NOT work correctly with different versions.

## How to interact with it
To start a game, choose a difficulty and press "start". At any point, if you want to restart or change
difficulties, click the desired difficultly and "restart".

To clear a tile, left-click the tile on the grid. Once a tile is cleared (assuming it's not a mine), the 
number displayed on the tile shows the number of mines adjacent to it. If the tile is blank, then there 
are no adjacent mines.

To flag a tile as a possible mine, right-click the tile. While flagged, the tile cannot be revealed to avoid misclicks.

The player wins once all the clear tiles have been revealed, and loses if a mine is clicked. 

The "File" dropdown in the Menu contains an option to enable fullscreen mode.