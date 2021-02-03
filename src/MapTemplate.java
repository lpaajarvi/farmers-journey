package fi.tuni.tiko.digging;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

// will determine how many Areas, and what kind, there will be in each Stages map (GameTile[][] tiles) there will be

//will also determine probability of non-stone tiles being blank tiles instead of dirt tiles

//will also determine hazardTemplate (chances of different hazards being generated)

public class MapTemplate {

    ArrayList <AreaTemplate> areaTemplates;

    private TerrainTemplate terrainTemplate;
    private HazardTemplate hazardTemplate;



    public HazardTemplate getHazardTemplate () {
        return hazardTemplate;
    }

    public void setHazardTemplate (HazardTemplate hazardTemplate) {
        this.hazardTemplate = hazardTemplate;
    }

    public TerrainTemplate getTerrainTemplate () {
        return terrainTemplate;
    }

    public void setTerrainTemplate (TerrainTemplate terrainTemplate) {
        this.terrainTemplate = terrainTemplate;
    }

    //this method could use premade templates instead of randoming like in default constructor
    public MapTemplate(int episode, int level) {

        areaTemplates = new ArrayList<AreaTemplate>();

        int chanceOfDesc = 0;
        int chanceOfStone = 0;
        int number = 0;

        if (level >= 2 && level <= 4) {
            number = 2;
        } else if (level == 5) {
            number = 3;
        } else if (level >= 6 && level <= 9) {
            number = 4;
        } else if (level == 10) {
            number = 10;
        }


        if (number == 2) {

            chanceOfDesc = 15;
            chanceOfStone = 9;

        } else if (number == 3) {
            chanceOfDesc = 80;
            chanceOfStone = 0;
        } else if (number == 4) {
            chanceOfDesc = 28;
            chanceOfStone = 9;
        } else if (number == 10) {
            chanceOfStone = 100;
        } else throw new IllegalArgumentException("invalid mapTemplate number");

        int randomRoll = MathUtils.random(1, 100);
        if (randomRoll <= chanceOfDesc) {
            if (episode >= 4) {
                terrainTemplate=new TerrainTemplate(11);
            } else
            terrainTemplate = new TerrainTemplate(1);
        } else if (randomRoll <= chanceOfStone + chanceOfDesc) {
            if (episode >= 4) {
                terrainTemplate = new TerrainTemplate(12);
            } else {
                terrainTemplate = new TerrainTemplate(2);
            }


        } else if (episode >= 4) {
            terrainTemplate=new TerrainTemplate(10);
        } else {
            terrainTemplate = new TerrainTemplate();
        }

        if (number == 10) {
            //it would be fun to have more areas but the phone starts to slow down easier
            generateAreaTemplates(6,8);

        } else {

            generateAreaTemplates(6,8);
        }

        hazardTemplate = new HazardTemplate(episode, level);
    }


    //constructor that makes (somewhat random number number of) ONLY areas as random as possible
    //AND uses default hazardTemplate
    public MapTemplate() {

        areaTemplates = new ArrayList<AreaTemplate>();
        terrainTemplate = new TerrainTemplate();
        hazardTemplate = new HazardTemplate();

        //terrainTemplate.setChanceOfDirtTileBeingBlank(30);

        // laitoin vaan 3,6 testin vuoksi, myöhemmin vähän enemmän

        generateAreaTemplates(6,8);


    }

    public void generateAreaTemplates (int min, int max) {
        int numberOfAreas = MathUtils.random(min, max);
        for (int i=0; i < numberOfAreas; i++) {
            AreaTemplate generalTemplate = new AreaTemplate();
            areaTemplates.add(generalTemplate);
        }

    }



    /*
    public MapTemplate getAreaTemplate(int number) {
        return new MapTemplate(number);
    }*/


    //en tiä onko paras nimi
    public ArrayList<AreaTemplate> getArrayList() {
        return areaTemplates;
    }


}
