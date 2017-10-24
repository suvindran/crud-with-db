package example.in.foodconnectdb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Acer on 31-07-2017.
 */


//recyclerView is used to create the list & cards//
//recyclerView wants Adapter,Model,Activity//
public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
    List<Model> list;
    Context context;
    int id;
    DBhelper db;


    public Adapter(List<Model> list, Context context) {
        this.list = list;
        this.context = context;


    }

    @Override
    public Adapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_list, parent, false);

        return new Holder(view);


    }

    @Override
    //holder is pick & paste the values in view textview//
    public void onBindViewHolder(Adapter.Holder holder, final int position) {
        holder.food_name.setText(list.get(position).getFoodname());
        holder.rate.setText(list.get(position).getRate().toString());
        holder.id.setText(String.valueOf(list.get(position).getId()));

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra("name", list.get(position).getFoodname().toString());
                intent.putExtra("rate", list.get(position).getRate().toString());
                intent.putExtra("id", String.valueOf(list.get(position).getId()));
                context.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(String.valueOf(list.get(position).getId()),id);
            }
        });

    }

    public void delete(final String id, final int position) {
        String tag_json_obj = "json_obj_req";

        String url = "http://192.168.1.105/shared/passport/public/api/food/delete";

        final SharedPreferences sp = context.getSharedPreferences("mypref", context.MODE_PRIVATE);
        db = new DBhelper(context);

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("id", id);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        try {
                            if ((response.get("status").toString()).equals("1")) {
                                list.remove(position);
                                notifyDataSetChanged();
                                db.delete(String.valueOf(list.get(position).getId()));



                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Response", "Error: " + error.getMessage());
                // hide the progress dialog
            }

        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<String, String>();
                header.put("Accept", "application/json");
                header.put("Authorization", "Bearer " + sp.getString("token", ""));
                return header;
            }
        };

// Adding request to request queue
        Appcontroller.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView food_name, rate,id;
        ImageButton edit, delete;

        public Holder(View itemView) {
            super(itemView);
            food_name = (TextView) itemView.findViewById(R.id.food_name);
            rate = (TextView) itemView.findViewById(R.id.rate);
            id = (TextView) itemView.findViewById(R.id.id);

            edit = (ImageButton) itemView.findViewById(R.id.edit);
            delete = (ImageButton) itemView.findViewById(R.id.delete);
        }
    }
}
