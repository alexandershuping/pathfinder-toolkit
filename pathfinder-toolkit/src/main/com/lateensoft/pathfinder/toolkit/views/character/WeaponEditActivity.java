package com.lateensoft.pathfinder.toolkit.views.character;

import android.content.res.Resources;
import android.os.Parcelable;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.lateensoft.pathfinder.toolkit.R;
import com.lateensoft.pathfinder.toolkit.model.character.items.Size;
import com.lateensoft.pathfinder.toolkit.model.character.items.Weapon;
import com.lateensoft.pathfinder.toolkit.model.character.items.WeaponType;
import com.lateensoft.pathfinder.toolkit.views.ParcelableEditorActivity;

public class WeaponEditActivity extends ParcelableEditorActivity {
    @SuppressWarnings("unused")
    private static final String TAG = WeaponEditActivity.class.getSimpleName();

    private static final int ATK_BONUS_OFFSET = 10;
    
    private Spinner m_highestAtkSpinner;
    private EditText m_ammoET;
    private Spinner m_sizeSpinner;
    private Spinner m_typeSpinner;
    private EditText m_rangeET;
    private EditText m_weightET;
    private EditText m_nameET;
    private EditText m_damageET;
    private EditText m_criticalET;
    private EditText m_quantityET;
    private CheckBox m_itemContainedCheckbox;
    private EditText m_specialPropertiesET;

    private Weapon m_weapon;
    private boolean m_weaponIsNew = false;
    
    @Override
    protected void setupContentView() {
        setContentView(R.layout.weapon_editor);

        m_nameET = (EditText) findViewById(R.id.etWeaponName);
        m_highestAtkSpinner = (Spinner) findViewById(R.id.spWeaponHighestAtk);
        m_ammoET = (EditText) findViewById(R.id.etWeaponAmmo);
        m_typeSpinner = (Spinner) findViewById(R.id.spWeaponType);
        m_sizeSpinner = (Spinner) findViewById(R.id.spWeaponSize);
        m_rangeET = (EditText) findViewById(R.id.etWeaponRange);
        m_criticalET = (EditText) findViewById(R.id.etWeaponCrit);
        m_damageET = (EditText) findViewById(R.id.etWeaponDamage);
        m_weightET = (EditText) findViewById(R.id.etWeaponWeight);
        m_quantityET = (EditText) findViewById(R.id.etItemQuantity);
        m_itemContainedCheckbox = (CheckBox) findViewById(R.id.checkboxItemContained);
        m_specialPropertiesET = (EditText) findViewById(
                R.id.etWeaponSpecialProperties);
        
        setupSpinner(m_highestAtkSpinner, R.array.weapon_attack_bonus_options,
                ATK_BONUS_OFFSET, null);
        Resources resources = getResources();
        setupSpinner(m_sizeSpinner, Size.getValuesSortedNames(resources), 0, null);
        setupSpinner(m_typeSpinner, WeaponType.getValuesSortedNames(resources), 0, null);

        if(m_weaponIsNew) {
            setTitle(R.string.new_weapon_title);
            m_highestAtkSpinner.setSelection(ATK_BONUS_OFFSET);
        } else {
            setTitle(R.string.edit_weapon_title);
            m_nameET.setText(m_weapon.getName());
            m_ammoET.setText(Integer.toString(m_weapon.getAmmunition()));            
            m_highestAtkSpinner.setSelection(m_weapon.getTotalAttackBonus() + ATK_BONUS_OFFSET);
            m_damageET.setText(m_weapon.getDamage());
            m_criticalET.setText(m_weapon.getCritical());
            m_sizeSpinner.setSelection(m_weapon.getSize().getValuesIndex());
            m_rangeET.setText(Integer.toString(m_weapon.getRange()));
            m_typeSpinner.setSelection(getWeaponTypeIndex(m_weapon.getType()));
            m_weightET.setText(Double.toString(m_weapon.getWeight()));
            m_quantityET.setText(Integer.toString(m_weapon.getQuantity()));
            m_itemContainedCheckbox.setChecked(m_weapon.isContained());
            m_specialPropertiesET.setText(m_weapon.getSpecialProperties());
        }
    }

    public int getWeaponTypeIndex(WeaponType type) {
        WeaponType[] types = WeaponType.values();
        for(int i = 0; i < types.length; i++) {
            if(type == types[i])
                return i;
        }
        throw new IllegalArgumentException("Invalid weapon type");
    }

    private String getStringByResourceAndIndex(int resourceId, int position) {
        Resources r = getResources();
        String[] resource = r.getStringArray(resourceId);
        return resource[position];
    }

    @Override
    protected void updateEditedParcelableValues() throws InvalidValueException {
        String name = m_nameET.getText().toString();
        
        if(name == null || name.isEmpty()) {
            throw new InvalidValueException(getString(R.string.editor_name_required_alert));
        }
        
        int attack = m_highestAtkSpinner.getSelectedItemPosition() - ATK_BONUS_OFFSET;;
        double weight;
        try {
            weight = Double.parseDouble(m_weightET.getText().toString());
        } catch (NumberFormatException e) {
            weight = 1;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(m_quantityET.getText().toString());
        } catch (NumberFormatException e) {
            quantity = 1;
        }

        int range = m_weapon.getRange();
        try {
            range = Integer.parseInt(m_rangeET.getText().toString());
        } catch (NumberFormatException e) {
            // Do not change range
        }

        int ammo = m_weapon.getAmmunition();
        try {
            ammo = Integer.parseInt(m_ammoET.getText().toString());
        } catch (NumberFormatException e) {
            // Do not change ammo
        }
        String damage = m_damageET.getText().toString();
        String critical = m_criticalET.getText().toString();
        String specialProperties = m_specialPropertiesET.getText().toString();
        int typeIndex = m_typeSpinner.getSelectedItemPosition();
        int sizeIndex = m_sizeSpinner.getSelectedItemPosition();
        
        m_weapon.setName(name);
        m_weapon.setTotalAttackBonus(attack);
        m_weapon.setWeight(weight);
        m_weapon.setRange(range);
        m_weapon.setAmmunition(ammo);
        m_weapon.setDamage(damage);
        m_weapon.setCritical(critical);
        m_weapon.setSpecialProperties(specialProperties);
        m_weapon.setType(weaponTypeForIndex(typeIndex));
        m_weapon.setSize(Size.forValuesIndex(sizeIndex));
        m_weapon.setQuantity(quantity);
        m_weapon.setContained(m_itemContainedCheckbox.isChecked());
    }

    public static WeaponType weaponTypeForIndex(int index) {
        return WeaponType.values()[index];
    }

    @Override
    protected Parcelable getEditedParcelable() {
        return m_weapon;
    }

    @Override
    protected void setParcelableToEdit(Parcelable p) {
        if (p == null) {
            m_weaponIsNew = true;
            m_weapon = new Weapon();
        } else {
            m_weapon = (Weapon) p;
        }
    }

    @Override
    protected boolean isParcelableDeletable() {
        return !m_weaponIsNew;
    }

}
