/* Game Class Starter File
 * Authors: Almog Zegaya
 * Last Edit: 5/26/25
 * Added example for using grid method setAllMarks()
 */

//import processing.sound.*;

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
  PImage splashBg;
  String splashBgFile = "images/apcsa.png";
  //SoundFile song;

  // VARIABLES: grid1 Screen (pieces on a grid pattern)
  Grid grid1;
  PImage grid1Bg;
  String grid1BgFile = "images/BattleshipBG.jpg";
  PImage piece1;   // Use PImage to display the image in a GridLocation
  String piece1File = "images/x_wood.png";
  int piece1Row = 3;
  int piece1Col = 0;
  AnimatedSprite chick;
  String chickFile = "sprites/chick_walk.png";
  String chickJson = "sprites/chick_walk.json";
  int chickRow = 0;
  int chickCol = 2;
  int health = 3;
  Button b1;

  // VARIABLES: skyWorld Screen (characters move by pixels)
  World skyWorld;
  PImage skyWorldBg;
  String skyWorldBgFile = "images/sky.png";
  Sprite zapdos; //Use Sprite for a pixel-based Location
  String zapdosFile = "images/zapdos.png";
  int zapdosStartX = 50;
  int zapdosStartY = 300;

  //VARIABLES: brickWorld Screen (characters jump on platforms with gravity)
  World brickWorld;
  PImage brickWorldBg;
  String brickWorldBgFile = "images/wall.jpg";
  Platform plat;

  // VARIABLES: endScreen
  World endScreen;
  PImage endBg;
  String endBgFile = "images/youwin.png";


  // VARIABLES: Tracking the current Screen being displayed
  Screen currentScreen;
  CycleTimer slowCycleTimer;

  boolean start = true;


  //------------------ REQUIRED PROCESSING METHODS --------------------//

  // Processing method that runs once for screen resolution settings
  public void settings() {
    //SETUP: Match the screen size to the background image size
    size(600,800);  //these will automatically be saved as width & height

    // Allows p variable to be used by other classes to access PApplet methods
    p = this;
    
  }

  //Required Processing method that gets run once
  public void setup() {

    p.imageMode(p.CORNER);    //Set Images to read coordinates at corners
    //fullScreen();   //only use if not using a specfic bg image
    
    //SETUP: Set the title on the title bar
    surface.setTitle(titleText);

    //SETUP: Load BG images used in all screens
    splashBg = p.loadImage(splashBgFile);
    grid1Bg = p.loadImage(grid1BgFile);
    skyWorldBg = p.loadImage(skyWorldBgFile);
    brickWorldBg = loadImage(brickWorldBgFile);
    endBg = p.loadImage(endBgFile);

    //SETUP: If non-moving, Resize all BG images to exactly match the screen size
    splashBg.resize(p.width, p.height);
    grid1Bg.resize(p.width, p.height);
    brickWorldBg.resize(p.width, p.height);
    endBg.resize(p.width, p.height);   

    //SETUP: Construct each Screen, World, Grid
    splashScreen = new Screen(p, "splash", splashBg);
    grid1 = new Grid(p, "chessBoard", grid1Bg, 10, 5);
    skyWorld = new World(p, "sky", skyWorldBgFile, 4.0f, 0.0f, -800.0f); //moveable World constructor
    brickWorld = new World(p,"platformer", brickWorldBg);
    endScreen = new World(p, "end", endBg);
    currentScreen = splashScreen;

    //SETUP: Construct Game objects used in All Screens
    runningHorse = new AnimatedSprite(p, "sprites/horse_run.png", "sprites/horse_run.json", 50.0f, 75.0f, 1.0f);

    //SETUP: Setup more grid1 objects
    // piece1 = p.loadImage(piece1File);
    // piece1.resize(grid1.getTileWidth(),grid1.getTileHeight());
    // chick = new AnimatedSprite(p, chickFile, chickJson, 0.0f, 0.0f, 0.5f);
    // grid1.setTileSprite(new GridLocation (chickRow, chickCol), chick);
    b1 = new Button(p, "rect", 625, 525, 150, 50, "GoTo Level 2");
    grid1.addSprite(b1);
    // b1.setFontStyle("fonts/spidermanFont.ttf");
    b1.setFontStyle("Calibri");
    b1.setTextColor(PColor.WHITE);
    b1.setButtonColor(PColor.BLACK);
    b1.setHoverColor(PColor.get(100,50,200));
    b1.setOutlineColor(PColor.WHITE);
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
    
    //SETUP: Setup more skyWorld objects
    zapdos = new Sprite(p, zapdosFile, 0.25f);
    zapdos.moveTo(zapdosStartX, zapdosStartY);
    skyWorld.addSprite(zapdos);
    skyWorld.addSpriteCopyTo(runningHorse, 100, 200);  //example Sprite added to a World at a location, with a speed
    skyWorld.printWorldSprites();
    System.out.println("Done loading Level 2 (skyWorld)...");

    // SETUP: Setup more brickWorld objects
    plat = new Platform(p, PColor.MAGENTA, 500.0f, 100.0f, 200.0f, 20.0f);
    plat.setOutlineColor(PColor.BLACK);
    plat.startGravity(5.0f); //sets gravity to a rate of 5.0
    brickWorld.addSprite(plat);    
    System.out.println("Done loading Level 3 (brickWorld)...");


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
    if(isGameOver()){
      endGame();
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

      // if the 'n' key is pressed, ask for their name
      if(p.key == 'n'){
        name = Input.getString("What is your name?");
      }

      // if the 't' key is pressed, then toggle the animation on/off
      if(p.key == 't'){
        //Toggle the animation on & off
        doAnimation = !doAnimation;
        System.out.println("doAnimation: " + doAnimation);
      }



    }

    if(currentScreen == brickWorld){
      if(p.key == 'w'){
        plat.jump();
      }
    }

    //CHANGING SCREENS BASED ON KEYS
    //change to level1 if 1 key pressed, level2 if 2 key is pressed
    if(p.key == '1'){
      currentScreen = grid1;
    } else if(p.key == '2'){
      currentScreen = skyWorld;
    } else if(p.key == '3'){
      currentScreen = brickWorld;

      //reset the moving Platform every time the Screen is re-displayed
      plat.moveTo(500.0f, 100.0f);
      plat.setSpeed(0,0);
    }

  }

  // Known Processing method that automatically will run when a mouse click triggers it
  public void mouseClicked(){
    
    // Print coordinates of mouse click
    System.out.println("\nMouse was clicked at (" + p.mouseX + "," + p.mouseY + ")");

    // Display color of pixel clicked
    int color = p.get(p.mouseX, p.mouseY);
    PColor.printPColor(p, color);

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
      piece1Row = grid1.getGridLocation().getRow();
      piece1Col = grid1.getGridLocation().getCol();
    }
    

  }



  //------------------ CUSTOM  GAME METHODS --------------------//

  // Updates the title bar of the Game
  public void updateTitleBar(){

    if(!isGameOver()) {

      extraText = currentScreen.getName();

      //set the title each loop
      surface.setTitle(titleText + "\t// CurrentScreen: " + extraText + " \t // Name: " + name + "\t // Health: " + health );

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
      GridLocation piece1Loc = new GridLocation(piece1Row,piece1Col);
      grid1.setTileImage(piece1Loc, piece1);

      // Displays the chick image
      GridLocation chickLoc = new GridLocation(chickRow, chickCol);
      grid1.setTileSprite(chickLoc, chick);

      // Moves to next level based on a button click
      // b1.show();
      if(b1.isClicked()){
        System.out.println("\nButton Clicked");
        currentScreen = skyWorld;
      }
    
    }
    
    // UPDATE: skyWorld Screen
    if(currentScreen == skyWorld){

      // Print a '2' in console when skyWorld
      System.out.print("2");

      // Set speed of moving skyWorld background
      skyWorld.moveBgXY(-0.3f, 0f);

    }

    // UPDATE: brickWorld Screen
    if(currentScreen == brickWorld){

      // Print a '3 in console when brickWorld
      System.out.print("3");


    }

    // UPDATE: End Screen
    // if(currentScreen == endScreen){

    // }

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
  public boolean isGameOver(){
    
    return false; //by default, the game is never over
  }

  // Describes what happens after the game is over
  public void endGame(){
      System.out.println("Game Over!");

      // Update the title bar

      // Show any end imagery
      currentScreen = endScreen;

  }


} //close class
