package com.lateensoft.pathfinder.toolkit.model.character;

import java.util.List;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.collect.Lists;
import com.lateensoft.pathfinder.toolkit.model.character.items.Armor;
import com.lateensoft.pathfinder.toolkit.model.character.items.Item;
import com.lateensoft.pathfinder.toolkit.model.character.items.Weapon;

public class Inventory implements Parcelable {
    private static final String PARCEL_BUNDLE_KEY_ITEMS = "items";
    private static final String PARCEL_BUNDLE_KEY_ARMOR = "armor";
    private static final String PARCEL_BUNDLE_KEY_WEAPONS = "weapons";
    
    private List<Item> m_items;
    private List<Armor> m_armor;
    private List<Weapon> m_weapons;



    public Inventory(){
        m_items = Lists.newArrayList();
        m_armor = Lists.newArrayList();
        m_weapons = Lists.newArrayList();
    }
    
    public Inventory(Parcel in) {
        Bundle objectBundle = in.readBundle();
        m_items = objectBundle.getParcelableArrayList(PARCEL_BUNDLE_KEY_ITEMS);
        m_armor = objectBundle.getParcelableArrayList(PARCEL_BUNDLE_KEY_ARMOR);
        m_weapons = objectBundle.getParcelableArrayList(PARCEL_BUNDLE_KEY_WEAPONS);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        Bundle objectBundle = new Bundle();
        objectBundle.putParcelableArrayList(PARCEL_BUNDLE_KEY_ITEMS, Lists.newArrayList(m_items));
        objectBundle.putParcelableArrayList(PARCEL_BUNDLE_KEY_ARMOR, Lists.newArrayList(m_armor));
        objectBundle.putParcelableArrayList(PARCEL_BUNDLE_KEY_WEAPONS, Lists.newArrayList(m_weapons));
        out.writeBundle(objectBundle);
    }

    public List<Item> getItems() {
        return m_items;
    }

    public List<Armor> getArmors() {
        return m_armor;
    }

    public List<Weapon> getWeapons() {
        return m_weapons;
    }

    public double getTotalWeight() {
        double totalWeight = 0;
        for (Item item : m_items) {
            if (!item.isContained())
                totalWeight += (item.getWeight()) * (item.getQuantity());
        }
        for (Weapon weapon : m_weapons) {
            if (!weapon.isContained())
                totalWeight += (weapon.getWeight()) * (weapon.getQuantity());
        }
        for (Armor armor : m_armor) {
            if (!armor.isContained())
                totalWeight += (armor.getWeight()) * (armor.getQuantity());
        }
        
        return totalWeight;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    public static final Parcelable.Creator<Inventory> CREATOR = new Parcelable.Creator<Inventory>() {
        public Inventory createFromParcel(Parcel in) {
            return new Inventory(in);
        }
        
        public Inventory[] newArray(int size) {
            return new Inventory[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inventory)) return false;

        Inventory inventory = (Inventory) o;

        if (!m_armor.equals(inventory.m_armor)) return false;
        if (!m_items.equals(inventory.m_items)) return false;
        if (!m_weapons.equals(inventory.m_weapons)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = m_items.hashCode();
        result = 31 * result + m_armor.hashCode();
        result = 31 * result + m_weapons.hashCode();
        return result;
    }
}
