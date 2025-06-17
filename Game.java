/* Game Class Starter File
 * Authors: Almog Zegaya
 * Last Edit: 5/26/25
 * Added example for using grid method setAllMarks()
 */

//import processing.sound.*;
import processing.core.PConstants;
import processing.core.PApplet;
import processing.core.PImage;

public class Game extends PApplet{

  //------------------ GAME VARIABLES --------------------//

  // VARIABLES: Processing variable to do Processing things
  PApplet p;

  // VARIABLES: Title Bar
  String titleText = "Battleship";
  String extraText = "CurrentLevel?";
  String name = "Undefined";

  // VARIABLES: Whole Game
  AnimatedSprite runningHorse;
  boolean doAnimation;

  // VARIABLES: splashScreen
  Screen splashScreen;
  String splashBgFile = "images/apcsa.png";
  Sprite gridImage;

  //SoundFile song;

  // VARIABLES: grid1 Screen (pieces on a grid pattern)
  Grid grid1;
  PImage grid1Bg;
  String grid1BgFile = "images/BattleshipBG.jpg";
  PImage piece1;   // Use PImage to display the image in a GridLocation
  String piece1File = "images/x_wood.png";
  int gridHeight = 10;
  int gridWidth = 5;
  int targetX = (int)(Math.random() * gridWidth) ;
  int targetY = (int)(Math.random() * gridHeight) ;
  AnimatedSprite chick;
  String chickFile = "sprites/chick_walk.png";
  String chickJson = "sprites/chick_walk.json";
  int chickRow = 0;
  int chickCol = 2;
  int health = 3;
  // Button b1;
  int clicks = 0;
  int rings = -1;
  PImage missImage ;
  PImage hitImage;

  // VARIABLES: endScreen
  Screen winScreen;
  Screen loseScreen;
  String winBgFile = "images/youwin.png";
  String loseBgFile = "images/wall.jpg";


  // VARIABLES: Tracking the current Screen being displayed
  Screen currentScreen;
  CycleTimer slowCycleTimer;

  boolean start = true;


  //------------------ REQUIRED PROCESSING METHODS --------------------//

  // Processing method that runs once for screen resolution settings
  public void settings() {
    //SETUP: Match the screen size to the background image size
    size(400,800);  //these will automatically be saved as width & height

    // Allows p variable to be used by other classes to access PApplet methods
    p = this;
    
  }

  //Required Processing method that gets run once
  public void setup() {

    System.out.println("Target:\t x:" + targetX + ", y:" + targetY);
    
    //SETUP: Set the title on the title bar
    surface.setTitle(titleText);
    p.imageMode(PConstants.CORNER);    //Set Images to read coordinates at corners

    //SETUP: Construct each Screen, World, Grid
    splashScreen = new Screen(p, "splash", splashBgFile);
    grid1 = new Grid(p, "chessBoard", grid1BgFile, gridHeight, gridWidth);
    winScreen = new Screen(p, "win", winBgFile);
    loseScreen = new Screen(p, "Lose", loseBgFile);
    currentScreen = splashScreen;

    gridImage = new Sprite(p, "images/grid.png", 1.25f, 0,35);
    gridImage.resize(400, 400);
    grid1.addSprite(gridImage);

    Sprite g2 = gridImage.copy();
    g2.moveTo(0, 400);
    grid1.addSprite(g2);

    //SETUP: Construct Game objects used in All Screens
    runningHorse = new AnimatedSprite(p, "sprites/horse_run.png", "sprites/horse_run.json", 50.0f, 75.0f, 1.0f);

    //SETUP: Setup more grid1 objects
    missImage = loadImage("images/Miss.png");
    missImage.resize(grid1.getTileWidth(), grid1.getTileHeight());
    hitImage = loadImage("images/Hit.png");
    hitImage.resize(grid1.getTileWidth(), grid1.getTileHeight());
    // b1 = new Button(p, "rect", 625, 525, 150, 50, "GoTo Level 2");
    // grid1.addSprite(b1);
    // // b1.setFontStyle("fonts/spidermanFont.ttf");
    // b1.setFontStyle("Calibri");
    // b1.setTextColor(PColor.WHITE);
    // b1.setButtonColor(PColor.BLACK);
    // b1.setHoverColor(PColor.get(100,50,200));
    // b1.setOutlineColor(PColor.WHITE);
    String[][] tileMarks = {
      {"R","N","B","Q","K","B","N","R"},
      {"P","P","P","P","P","P","P","P"},
      {"", "", "", "", "", "", "", ""},
      {"", "", "", "", "", "", "", ""},
      {"P","P","P","P","P","P","P","P"},
      {"R","N","B","Q","K","B","N","R"}
    };
    grid1.setAllMarks(tileMarks);
    grid1.startPrintingGridMarks();
    System.out.println("Done loading Level 1 (grid1)...");
    


    //SETUP: Sound
    // Load a soundfile from the sounds folder of the sketch and play it back
     //song = new SoundFile(p, "sounds/Lenny_Kravitz_Fly_Away.mp3");
     //song.play();
    
    System.out.println("Game started...");

  } //end setup()


  //Required Processing method that automatically loops
  //(Anything drawn on the screen should be called from here)
  public void draw() {

    // DRAW LOOP: Update Screen Visuals
    updateTitleBar();
    updateScreen();

    // DRAW LOOP: Set Timers
    int cycleTime = 1;  //milliseconds
    int slowCycleTime = 300;  //milliseconds
    if(slowCycleTimer == null){
      slowCycleTimer = new CycleTimer(p, slowCycleTime);
    }

    // DRAW LOOP: Populate & Move Sprites
    if(slowCycleTimer.isDone()){
      populateSprites();
      moveSprites();
    }

    // DRAW LOOP: Pause Game Cycle
    currentScreen.pause(cycleTime);   // slows down the game cycles

    // DRAW LOOP: Check for end of game
    if(didIWin()){
      winGame();
    }
    if(didILose()){
      loseGame();
    }

  } //end draw()

  //------------------ USER INPUT METHODS --------------------//


  //Known Processing method that automatically will run whenever a key is pressed
  public void keyPressed(){

    //check what key was pressed
    System.out.println("\nKey pressed: " + p.keyCode); //key gives you a character for the key pressed

    //What to do when a key is pressed?
    
    //KEYS FOR LEVEL1
    if(currentScreen == grid1){

      //set [S] key to move the chick down & avoid Out-of-Bounds errors
      if(p.keyCode == 83){        

        //change the field for chickRow
        chickRow++;
      }



      // if the 't' key is pressed, then toggle the animation on/off
      if(p.key == 't'){
        //Toggle the animation on & off
        doAnimation = !doAnimation;
        System.out.println("doAnimation: " + doAnimation);
      }



    }


    //CHANGING SCREENS BASED ON KEYS
    //change to level1 if 1 key pressed, level2 if 2 key is pressed
    if(p.key == '1'){
      currentScreen = grid1;
    } 
  }

  // Known Processing method that automatically will run when a mouse click triggers it
  public void mouseClicked(){
    
    // Print coordinates of mouse click
    System.out.println("\nMouse was clicked at (" + p.mouseX + "," + p.mouseY + ")");

    // Display color of pixel clicked
    // int color = p.get(p.mouseX, p.mouseY);
    // PColor.printPColor(p, color);

    // if the Screen is a Grid, print grid coordinate clicked
    if(currentScreen instanceof Grid){
      System.out.println("Grid location --> " + ((Grid) currentScreen).getGridLocation());
    }

    // if the Screen is a Grid, "mark" the grid coordinate to track the state of the Grid
    if(currentScreen instanceof Grid){
      ((Grid) currentScreen).setMark("X",((Grid) currentScreen).getGridLocation());
    }

    // what to do if clicked? (ex. assign a new location to piece1)
    if(currentScreen == grid1){

      GridLocation loc = ((Grid) currentScreen).getGridLocation();
      int clickedX = loc.getXCoord();
      int clickedY = loc.getYCoord();
      clicks++;
      System.out.println(clicks);

      rings = Math.max(Math.abs(clickedX - targetX), Math.abs(clickedY - targetY));
      System.out.println("rings" +rings);

      if(rings != 0){
        grid1.setTileImage(loc, missImage);
      }

    }

  
    

  }



  //------------------ CUSTOM  GAME METHODS --------------------//

  // Updates the title bar of the Game
  public void updateTitleBar(){

    if(!didIWin()) {


      //set the title each loop
      surface.setTitle(titleText + "\t// Clicks: " + clicks + " \t // Rings " + rings );

      //adjust the extra text as desired
    
    }
  }

  // Updates what is drawn on the screen each frame
  public void updateScreen(){

    // UPDATE: first lay down the Background
    currentScreen.showBg();

    // UPDATE: splashScreen
    if(currentScreen == splashScreen){

      // Print an s in console when splashscreen is up
      System.out.print("s");

      // Change the screen to level 1 between 3 and 5 seconds
      if(splashScreen.getScreenTime() > 3000 && splashScreen.getScreenTime() < 5000){
        currentScreen = grid1;
      }
    }

    // UPDATE: grid1 Screen
    if(currentScreen == grid1){

      // Print a '1' in console when level1
      System.out.print("1");

      // Displays the piece1 image
      GridLocation piece1Loc = new GridLocation(targetY, targetX);
      grid1.setTileImage(piece1Loc, piece1);

      // Displays the chick image
      GridLocation chickLoc = new GridLocation(chickRow, chickCol);
      grid1.setTileSprite(chickLoc, chick);

    }
    
 

    // UPDATE: Any Screen
    if(doAnimation){
      runningHorse.animateHorizontal(0.5f, 1.0f, true);
    }

    // UPDATE: Other built-in to current World/Grid/HexGrid
    currentScreen.show();

  }

  // Populates enemies or other sprites on the Screen
  public void populateSprites(){

    //What is the index for the last column?
    

    //Loop through all the rows in the last column

      //Generate a random number


      //10% of the time, decide to add an enemy image to a Tile
      

  }

  // Moves around the enemies/sprites on the Screen
  public void moveSprites(){

    //Loop through all of the rows & cols in the grid

        //Store the current GridLocation

        //Store the next GridLocation

        //Check if the current tile has an image that is not piece1      


          //Get image/sprite from current location
            

          //CASE 1: Collision with piece1


          //CASE 2: Move enemy over to new location


          //Erase image/sprite from old location

          //System.out.println(loc + " " + grid.hasTileImage(loc));

            
        //CASE 3: Enemy leaves screen at first column

  }

  // Checks if there is a collision between Sprites on the Screen
  public boolean checkCollision(GridLocation loc, GridLocation nextLoc){

    //Check what image/sprite is stored in the CURRENT location
    // PImage image = grid.getTileImage(loc);
    // AnimatedSprite sprite = grid.getTileSprite(loc);

    //if empty --> no collision

    //Check what image/sprite is stored in the NEXT location

    //if empty --> no collision

    //check if enemy runs into player

      //clear out the enemy if it hits the player (using cleartTileImage() or clearTileSprite() from Grid class)

      //Update status variable

    //check if a player collides into enemy

    return false; //<--default return
  }

  // Indicates when the main game is over
  public boolean didIWin(){

    if (rings == 0){
      return true;
    }
    
    return false; //by default, the game is never over
  }

  public boolean didILose(){

    if (clicks == 8){
      return true;
    }
    
    return false; //by default, the game is never over
  }


  // Describes what happens after the game is over
  public void winGame(){
      System.out.println("Game Over!");

      // Update the title bar

      // Show any end imagery
      currentScreen = winScreen;

  }
  // Describes what happens after the game is over
  public void loseGame(){
      System.out.println("Game Over!");

      // Update the title bar

      // Show any end imagery
      currentScreen = loseScreen;

  }

} //close class
