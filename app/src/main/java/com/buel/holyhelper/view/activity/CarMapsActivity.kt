package com.buel.holyhelper.view.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.buel.holyhelper.R
import com.buel.holyhelper.data.CommonData
import com.buel.holyhelper.data.CommonString
import com.buel.holyhelper.data.FDDatabaseHelper
import com.buel.holyhelper.management.firestore.FireStoreWriteManager
import com.buel.holyhelper.model.CarModel
import com.buel.holyhelper.utils.isPermissionGranted
import com.buel.holyhelper.view.DataTypeListener
import com.buel.holyhelper.view.recyclerView.memberShipRecyclerView.MemberShipRecyclerViewHolder
import com.commonLib.MaterialDailogUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_car_maps.*

class CarMapsActivity : BaseActivity(), OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    override fun onMarkerClick(p0: Marker?) = false

    private lateinit var mMap: GoogleMap
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_maps)

        setTopLayout(this)
        super.setTopTitleDesc("Car Manager")

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val tempList = ArrayList<String>()
        tempList.add("car_1")
        tempList.add("car_2")
        tempList.add("car_3")
        tempList.add("car_4")
        tempList.add("car_5")
        tempList.add("car_6")
        search_car.setOnClickListener {
            MaterialDailogUtil.showSingleChoice(this@CarMapsActivity, CommonString.CORP_NICK + " 을/를 선택하세요.",
                    tempList, object : MaterialDailogUtil.OnDialogSelectListner {
                override fun onSelect(s: String) {


                }
            })
        }

        add_car.setOnClickListener {
            var carModel = CarModel()
            carModel.carName = add_car_txt.text.toString()
            FireStoreWriteManager.insert(
                    FireStoreWriteManager.firestore.collection(FDDatabaseHelper.CORPS_TABLE).document(CommonData.getHolyModel().uid)
                            .collection(FDDatabaseHelper.CAR_TABLE).document(),carModel,
                    DataTypeListener.OnCompleteListener {
                        t ->
                        if(t)popToast("저장되었습니다.")
                    })
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.getUiSettings().setZoomControlsEnabled(true)
        map.setOnMarkerClickListener(this)

        //val myPlace = LatLng(40.73, -73.99)  // this is New York
        //map.addMarker(MarkerOptions().position(myPlace).title("My Favorite City"))
        //map.moveCamera(CameraUpdateFactory.newLatLng(myPlace))
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlace, 12.0f))

        /*mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/

        setUpMap()
    }

    lateinit internal var recyclerView: RecyclerView
    internal var holderAdapter: RecyclerView.Adapter<MemberShipRecyclerViewHolder>? = null

    private fun setRecyclerVeiw() {
        recyclerView = findViewById(R.id.recycler_view_main)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager


        val carModel = CarModel()
        /*val carModels:ArrayList<CarModel> = arrayListOf()
        holderAdapter = MemberShipRecyclerViewAdapter(this@CarMapsActivity, carModels , this)
        recyclerView.adapter = holderAdapter*/

    }


    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        // 1
        map.isMyLocationEnabled = true

        // 2
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            // 3

            Log.e("CarMapsActivity", "CarLocationManager")

            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    /*
     * 사용자가 PermmissionC Check 대화상자(허락,거부)에서 선택한 결과를
     * 처리하는 콜백 메소드
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        //요청코드가 맞지 않는다면
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }
        /*
         * PermissionCheckUtil.kt 파일의 isPermissionGranted 함수를 호출
         */
        if (isPermissionGranted(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), grantResults)) {
            //허락을 받았다면 위치값을 알아오는 코드를 진행
            setUpMap()
        } else { //사용자가 허락하지 않을경우
            Toast.makeText(this, "위치정보사용을 허락 하지않아 앱을 중지합니다",
                    Toast.LENGTH_SHORT).show()
            //finish();
        }
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.top_bar_btn_back ->
                //goBackHistoryIntent();
                goMain()
            else -> {
            }
        }//if (!readyToPurchase) return;
        //bp.subscribe(SettingsActivity.this, CommonString.SUB_ADMIN_SUBSCRIBE_01);
    }
}
