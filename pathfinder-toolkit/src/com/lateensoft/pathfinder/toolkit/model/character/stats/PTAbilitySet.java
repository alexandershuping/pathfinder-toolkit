package com.lateensoft.pathfinder.toolkit.model.character.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lateensoft.pathfinder.toolkit.PTBaseApplication;
import com.lateensoft.pathfinder.toolkit.R;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

public class PTAbilitySet implements Parcelable{
	@SuppressWarnings("unused")
	private static final String TAG = PTAbilitySet.class.getSimpleName();
	private static final String PARCEL_BUNDLE_KEY_ABILITIES = "abilities";
	
	public static final int KEY_STR = 1;
	public static final int KEY_DEX = 2;
	public static final int KEY_CON = 3;
	public static final int KEY_INT = 4;
	public static final int KEY_WIS = 5;
	public static final int KEY_CHA = 6;
	
	/**
	* This matches the order for string resources, and how the abilities are stored
	* in the set.
	*/
	public static final int[] ABILITY_KEYS = {KEY_STR, KEY_DEX, KEY_CON, KEY_INT, KEY_WIS, KEY_CHA};
	
	private PTAbility[] m_abilities;
	
	public PTAbilitySet() {
		m_abilities = new PTAbility[ABILITY_KEYS.length];
		
		for(int i = 0; i < ABILITY_KEYS.length; i++) {
			m_abilities[i] = new PTAbility(ABILITY_KEYS[i], PTAbility.BASE_ABILITY_SCORE, 0);
		}
	}
	
	/**
	 * Safely populates the ability set with scores 
	 * If an ability does not exist in scores, will be set to default.
	 * @param abilities
	 */
	public PTAbilitySet(PTAbility[] abilities) {
		m_abilities = new PTAbility[ABILITY_KEYS.length];
		List<PTAbility> scoresList = new ArrayList<PTAbility>(Arrays.asList(abilities));
		
		for(int i = 0; i < ABILITY_KEYS.length; i++) {
			for (PTAbility score : scoresList) {
				if(score.getAbilityKey() == ABILITY_KEYS[i]) {
					scoresList.remove(score);
					m_abilities[i] = score;
					break;
				}
			}
			if (m_abilities[i] == null) {
				m_abilities[i] = new PTAbility(ABILITY_KEYS[i]);
			}
		}
	}
	
	public PTAbilitySet(Parcel in) {
		Bundle objectBundle = in.readBundle();
		m_abilities = (PTAbility[]) objectBundle.getParcelableArray(PARCEL_BUNDLE_KEY_ABILITIES);
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		Bundle objectBundle = new Bundle();
		objectBundle.putParcelableArray(PARCEL_BUNDLE_KEY_ABILITIES, m_abilities);
		out.writeBundle(objectBundle);
	}
	
	/**
	 * @param abilityKey
	 * @return The ability in the set with abilityKey. null if no such ability exists.
	 */
	public PTAbility getAbility(int abilityKey) {
		for(int i = 0; i < m_abilities.length; i++) {
			if(abilityKey == m_abilities[i].getID()) {
				return m_abilities[i];
			}
		}
		return null;
	}
	
	/**
	 * @param index
	 * @return the ability at index. Note: indexes of the set are defined by ABILITY_KEYS
	 */
	public PTAbility getAbilityAtIndex(int index) {
		if( index >=0 && index <= m_abilities.length) {
			return m_abilities[index];
		} else {
			throw new IndexOutOfBoundsException("No ability for index "+index);
		}
	}
	
	/**
	 * @param maxDex maximum dex mod for the character
	 * @return the final mod value of the ability
	 */
	public int getTotalAbilityMod(int abilityKey, int maxDex) {
		int abilityMod = getAbility(abilityKey).getTempModifier();
		if (abilityKey == KEY_DEX && abilityMod > maxDex) {
			return maxDex;
		} else {
			return abilityMod;
		}
	}
	
	/**
	 * @return the short ability names, in the order as defined by ABILITY_KEYS
	 */
	public static String[] getShortAbilityNames() {
		Resources res = PTBaseApplication.getAppContext().getResources();
		return res.getStringArray(R.array.abilities_short);
	}
	
	/**
	 * @return a map of the ability keys to their short name
	 */
	public static SparseArray<String> getAbilityShortNameMap() {
		SparseArray<String> map = new SparseArray<String>(ABILITY_KEYS.length);
		String[] names = getShortAbilityNames();
		for (int i = 0; i < names.length; i++) {
			map.append(ABILITY_KEYS[i], names[i]);
		}
		return map;
	}	

	
	public int size(){
		return m_abilities.length;
	}
	
	public void setCharacterID(long id) {
		for (PTAbility ability : m_abilities) {
			ability.setCharacterID(id);
		}
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<PTAbilitySet> CREATOR = new Parcelable.Creator<PTAbilitySet>() {
		public PTAbilitySet createFromParcel(Parcel in) {
			return new PTAbilitySet(in);
		}
		
		public PTAbilitySet[] newArray(int size) {
			return new PTAbilitySet[size];
		}
	};
}