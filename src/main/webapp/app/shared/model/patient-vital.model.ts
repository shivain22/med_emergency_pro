import dayjs from 'dayjs';
import { IPatient } from 'app/shared/model/patient.model';

export interface IPatientVital {
  id?: number;
  pulseRate?: string | null;
  bloodPressure?: string | null;
  respiration?: string | null;
  spo2?: string | null;
  timeOfMeasurement?: string;
  patient?: IPatient;
}

export const defaultValue: Readonly<IPatientVital> = {};
