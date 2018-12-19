package vn.edu.uit.quanlychitieunhom.Views;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.uit.quanlychitieunhom.Adapters.SpinnerNhomChiTieu_Adapter;
import vn.edu.uit.quanlychitieunhom.ClientConfig.RetrofitClientInstance;
import vn.edu.uit.quanlychitieunhom.Models.giaodich;
import vn.edu.uit.quanlychitieunhom.Models.kychitieu;
import vn.edu.uit.quanlychitieunhom.Models.nhomchitieu;
import vn.edu.uit.quanlychitieunhom.Models.taikhoan;
import vn.edu.uit.quanlychitieunhom.R;
import vn.edu.uit.quanlychitieunhom.Services.GiaoDich_Service;
import vn.edu.uit.quanlychitieunhom.Services.KyChiTieu_Service;
import vn.edu.uit.quanlychitieunhom.Services.NhomChiTieu_Service;
import vn.edu.uit.quanlychitieunhom.Utils.Util;

public class ThietLapKiChiTieu extends AppCompatActivity {
    Util util = new Util();
    int StatusCode;
    int ERR=0;

    private ImageButton mPickDateFrom;
    private TextView mDateDisplayFrom;
    private ImageButton mPickDateTo;
    private TextView mDateDisplayTo;
    private DatePickerDialog datePickerDialog;
    private EditText txtHanmucchitieu;
    private EditText txtTenKyChiTieu;
    private Spinner spNhomChiTieu;
    private Button btnThemKyChiTieu;

    int year;
    int month;
    int day;
    Calendar calendar;

    private taikhoan user_admin;
    List<nhomchitieu> List_NhomChiTieu = new ArrayList<>();
    nhomchitieu NhomChiTieu = new nhomchitieu();
    private kychitieu new_kychitieu;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thiet_lap_ki_chi_tieu);
        getComponentByID();
        addControls();
        new getNhomChiTieuTask().execute();
    }


    public void getComponentByID(){
        mPickDateFrom =  findViewById(R.id.pickDateFrom);
        mDateDisplayFrom = findViewById(R.id.dateDisplayFrom);
        txtHanmucchitieu = findViewById(R.id.txtHanmucchitieu);
        mPickDateTo = findViewById(R.id.pickDateTo);
        mDateDisplayTo= findViewById(R.id.dateDisplayTo);
        spNhomChiTieu = findViewById(R.id.spNhomChiTieu);
        txtTenKyChiTieu = findViewById(R.id.txtTenKyChiTieu);
        btnThemKyChiTieu = findViewById(R.id.btnThemKyChiTieu);
    }


    private void addControls() {

        mPickDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get current day
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                //
                datePickerDialog = new DatePickerDialog(ThietLapKiChiTieu.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                mDateDisplayFrom.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        },year,month,day);
                datePickerDialog.show();
            }
        });

        mPickDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get current day
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                //
                datePickerDialog = new DatePickerDialog(ThietLapKiChiTieu.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                mDateDisplayTo.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        },year,month,day);
                datePickerDialog.show();
            }
        });

        btnThemKyChiTieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                checkValidate("Số tiền",txtSotien);
                Gson gson = new Gson();
                new insertKyChiTieuTask().execute();
                Toast.makeText(getApplicationContext(),gson.toJson(new_kychitieu),Toast.LENGTH_LONG).show();

                if(ERR == 0){
                    //TODO:
//                    new ThemGiaoDich.insertGiaoDichTask().execute();
                }
                else{
                    ERR = 0;
                }
            }
        });
    }


    //TODO: Initial for spinner Nhom chi tieu
    public void setSpinnerAdapter(){
        final SpinnerNhomChiTieu_Adapter spinnerAdapter = new SpinnerNhomChiTieu_Adapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item ,List_NhomChiTieu);
        spNhomChiTieu.setAdapter(spinnerAdapter);
        spNhomChiTieu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                NhomChiTieu = spinnerAdapter.getItem(position);//TODO: event click to selected item from spinner
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });
    }

    public class  getNhomChiTieuTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            user_admin = util.getUserLocalStorage(getApplicationContext());
        }
        @Override
        protected Boolean  doInBackground(String... params) {
            try {
                NhomChiTieu_Service service = RetrofitClientInstance.getRetrofitInstance().create(NhomChiTieu_Service.class);
                Call<List<nhomchitieu>> call = service.getAllNhomChiTieu(user_admin.getTentaikhoan());
                call.enqueue(new Callback<List<nhomchitieu>>() {
                    @Override
                    public void onResponse(Call<List<nhomchitieu>> call, Response<List<nhomchitieu>> response) {
                        StatusCode = response.code();
                        if(StatusCode== 200){
                            List_NhomChiTieu = response.body();
                            setSpinnerAdapter();
                        }
                    }
                    @Override
                    public void onFailure(Call<List<nhomchitieu>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"Có lỗi xảy ra. Vui lòng thao tác lại sau!", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.d("Test", "Exception");
            }
            // TODO: register the new account here.
            return (StatusCode == 200)? true : false;
        }
    }


    //TODO:Get information Ky chi tieu to insert
    public void GetInputKyChiTieu() throws ParseException {
        new_kychitieu = new kychitieu();
        new_kychitieu = new kychitieu(txtTenKyChiTieu.getText().toString(),
                                      util.StringToDate(mDateDisplayFrom.getText().toString(),"dd/MM/yyyy"),
                                      util.StringToDate(mDateDisplayTo.getText().toString(),"dd/MM/yyyy"),
                                      Double.parseDouble(txtHanmucchitieu.getText().toString()),
                                      NhomChiTieu);
    }


    //TODO: insert new Giao dich
    public class  insertKyChiTieuTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {

            try {
//                showProgress(true);
                GetInputKyChiTieu();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Boolean  doInBackground(String... params) {
            // TODO: register the new account here.
            try {
                Thread.sleep(2000);
                KyChiTieu_Service service = RetrofitClientInstance.getRetrofitInstance().create(KyChiTieu_Service.class);
                Call<Void> call = service.insert(new_kychitieu);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        StatusCode = response.code();
                        if(StatusCode == 200){
//                            showProgress(false);
//                            clearInput();
                            Toast.makeText(getApplicationContext(), "Thêm thành công !", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Log.d("ERROR",jObjError.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Log.d("test",String.valueOf(response.code()));
//                            showProgress(false);
                            Toast.makeText(getApplicationContext(), "Thêm thất bại !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
//                        showProgress(false);
                        Log.d("ERR",t.getMessage());
                        Toast.makeText(getApplicationContext(), "Có lỗi xảy ra. Vui lòng thao tác lại sau !", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.d("ERROR", e.toString());
            }
            finally {
//                util.setFlagNewGiaoDich(getApplicationContext(),true, kychitieu.getMakychitieu());
            }
            return (StatusCode == 200)? true : false;
        }

    }




}
