import { IPatient } from 'app/shared/model/patient.model';
import { IComorbidity } from 'app/shared/model/comorbidity.model';

export interface IPatientComorbidity {
  id?: number;
  patient?: IPatient;
  comorbidity?: IComorbidity;
}

export const defaultValue: Readonly<IPatientComorbidity> = {};
