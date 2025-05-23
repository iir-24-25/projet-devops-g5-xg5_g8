package com.example.sahti.models;

import com.example.sahti.activities.DoctorModel;

import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/api/patients/register")
    Call<ResponseBody> registerPatient(@Body RegisterPatient patient);

    @POST("/api/doctors/register")
    Call<ResponseBody> registerDoctor(@Body Doctor doctor);

    @POST("/api/doctors/login")
    Call<LoginResponse> loginDoctor(@Body LoginRequest request);

    @POST("/api/patients/login")
    Call<LoginResponse> loginPatient(@Body LoginRequest request);

    @GET("/api/doctors/{id}")
    Call<DoctorModel> getDoctorById(@Path("id") Long id);

    @GET("/api/doctors")
    Call<List<DoctorModel>> getDoctorsBySpeciality(@Query("speciality") String speciality);

    @GET("doctors/search")
    Call<List<DoctorModel>> searchDoctors(@Query("query") String query);

    @POST("/api/appointments/save")
    Call<Appointment> saveAppointment(@Header("Authorization") String token, @Body Appointment appointment);

    @GET("/api/appointments/validated/{doctorId}")
    Call<List<AppointmentWithDoctor>> getValidatedAppointmentsById(@Path("doctorId") String doctorId);

    // Save or update appointment (requires auth token)
    @POST("/api/appointments")
    Call<AppointmentWithDoctor> saveAppointment(@Header("Authorization") String token, @Body AppointmentWithDoctor appointment);

    // Get validated appointments by doctor ID
    @GET("/api/appointments/doctor/{doctorId}")
    Call<List<AppointmentWithDoctor>> getAppointmentsByDoctor(@Path("doctorId") String doctorId);

    // Get appointments by patient ID (optional)
    @GET("/api/appointments/patient/{patientId}")
    Call<List<AppointmentWithDoctor>> getAppointmentsByPatient(@Path("patientId") String patientId);

    // Delete appointment by ID (optional)
    @DELETE("/api/appointments/{id}")
    Call<ResponseBody> deleteAppointment(@Header("Authorization") String token, @Path("id") String appointmentId);
}
