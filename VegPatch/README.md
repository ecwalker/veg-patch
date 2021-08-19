# VegPatch App

Overview and purpose of app..

## General Info

### VegPatch

#### Permissions

1. Internet (for weather API)
2. Location (to use with weather API)

### Screenshots

### How to use app


## Project plan and milestones

### Project Plan

Proof of concept
1. Create MainActivity with RecyclerView
2. Implement Room database with DAO, DTO and repository
3. Use MainActivity as a template to create AddPlantActivity and PlantDetailActivity
4. Use explicit intents to navigate from MainActivity to both AddPlantActivity (FAB)
    and PlantDetailActivity (click RecyclerView list item)
5. Connect to weather API with RetroFit and save data locally into Room
6. Set up worker to schedule getting data from weather API
7. Send a notification based on severe weather forecast and active plants
8. MotionLayout... <- <- <-

Then add more data and add a polished UI
[x] Add data for plants
[x] Add button in PlantDetailActivity and database function to remove an active plant
[x] Order plantList alphabetically
[x] Add header in MainActivity with Summary (Num active plants) and weather summary icon
[x] Update weather warning logic to check for active plants (that are not frost hardy)
[] Implement weather warning activity






### Milestones