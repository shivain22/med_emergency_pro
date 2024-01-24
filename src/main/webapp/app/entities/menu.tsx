import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/patient">
        Patient
      </MenuItem>
      <MenuItem icon="asterisk" to="/disability">
        Disability
      </MenuItem>
      <MenuItem icon="asterisk" to="/comorbidity">
        Comorbidity
      </MenuItem>
      <MenuItem icon="asterisk" to="/patient-vital">
        Patient Vital
      </MenuItem>
      <MenuItem icon="asterisk" to="/comment">
        Comment
      </MenuItem>
      <MenuItem icon="asterisk" to="/patient-comorbidity">
        Patient Comorbidity
      </MenuItem>
      <MenuItem icon="asterisk" to="/patient-disability">
        Patient Disability
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
