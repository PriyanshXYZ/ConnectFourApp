#Connect-Four App
- **About** 
  - Connect Four is a two-player connection game in which the players first choose a color and then take turns dropping colored discs from the top into a seven-column, six-row vertically suspended grid. The pieces fall straight down, occupying the next available space within the column. The objective of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's own discs. Connect Four is a solved game. The first player can always win by playing the right moves.
- I used [JavaFX](https://docs.oracle.com/javafx/2/overview/jfxpub-overview.htm) package in with the help of [Scene builder](https://gluonhq.com/products/scene-builder/) to make this simple game.
- I used the `MINMAX Algorithm` in this.
- BASIC APP PREVIEW IS LIKE THIS
<img src="app_preview.JPG">
- the game ends when there is any possible combination of 4 dots like shown below
  - diagonal dots connected
    <img height="5" src="diagonal_combination.JPG" title="diagonal connect" width="5"/>
  - horizontal dots connected
    <img height="5" src="horizontal_combination.JPG" title="horizontal connect" width="5"/>
  - vertical dots connected
    <img height="5" src="vertical_combination.JPG" title="vertical connect" width="5"/>
- **_TODO :-_**
  - adding computer based AI in this.
  - adding to save the data of the user's win 
  - login page to the app.