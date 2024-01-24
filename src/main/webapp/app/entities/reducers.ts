import patient from 'app/entities/patient/patient.reducer';
import disability from 'app/entities/disability/disability.reducer';
import comorbidity from 'app/entities/comorbidity/comorbidity.reducer';
import patientVital from 'app/entities/patient-vital/patient-vital.reducer';
import comment from 'app/entities/comment/comment.reducer';
import patientComorbidity from 'app/entities/patient-comorbidity/patient-comorbidity.reducer';
import patientDisability from 'app/entities/patient-disability/patient-disability.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  patient,
  disability,
  comorbidity,
  patientVital,
  comment,
  patientComorbidity,
  patientDisability,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
