package models;

public class Inventory
{
    private int inventoryId;
    private String computerName;
    private int locationId;
    private String currentUser;
    private String buildingLocation;
    private int regionId;

    public Inventory(int inventoryId, String computerName, int locationId,
                     String currentUser, String buildingLocation, int regionId)
    {
        this.inventoryId = inventoryId;
        this.computerName = computerName;
        this.locationId = locationId;
        this.currentUser = currentUser;
        this.buildingLocation = buildingLocation;
        this.regionId = regionId;
    }

    public int getInventoryId()
    {
        return inventoryId;
    }


    public String getComputerName()
    {
        return computerName;
    }

    public void setComputerName(String computerName)
    {
        this.computerName = computerName;
    }

    public int getLocationId()
    {
        return locationId;
    }

    public void setLocationId(int locationId)
    {
        this.locationId = locationId;
    }

    public String getCurrentUser()
    {
        return currentUser;
    }

    public void setCurrentUser(String currentUser)
    {
        this.currentUser = currentUser;
    }

    public String getBuildingLocation()
    {
        return buildingLocation;
    }

    public void setBuildingLocation(String buildingLocation)
    {
        this.buildingLocation = buildingLocation;
    }

    public int getRegionId()
    {
        return regionId;
    }

    public void setRegionId(int regionId)
    {
        this.regionId = regionId;
    }
}
