package com.thesaugat.appcommerce.home.fragments.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.thesaugat.appcommerce.R;
import com.thesaugat.appcommerce.api.response.Product;
import com.thesaugat.appcommerce.singleProductPage.SingleProductActivity;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {
    List<Product> productDataList;
    LayoutInflater layoutInflater;
    Context context;
    Boolean isCart = false;
    CartItemClick cartItemClick;
    Boolean removeEnabled = true;

    public ShopAdapter(List<Product> productDataList, Context context, Boolean isCart) {
        this.productDataList = productDataList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.isCart = isCart;
    }

    public void setCartItemClick(CartItemClick cartItemClick) {
        this.cartItemClick = cartItemClick;
    }

    public void setRemoveEnabled(Boolean removeEnabled) {
        this.removeEnabled = removeEnabled;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isCart)
            return new ShopViewHolder(layoutInflater.inflate(R.layout.item_cart, parent, false));
        else
            return new ShopViewHolder(layoutInflater.inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        holder.nameTV.setText(productDataList.get(position).getName());
        if (productDataList.get(position).getDiscountPrice() == null || productDataList.get(position).getDiscountPrice() == 0) {
            holder.priceTv.setVisibility(View.GONE);
            holder.discountPrice.setText("Rs. " + productDataList.get(position).getPrice() + "");
        } else
            holder.discountPrice.setText("Rs. " + productDataList.get(position).getDiscountPrice() + "");
        holder.priceTv.setText(productDataList.get(position).getPrice() + "");

        Picasso.get().load(productDataList.get(position).getImages().get(0)).into(holder.productIV);
        holder.mainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productPage = new Intent(context, SingleProductActivity.class);
                productPage.putExtra(SingleProductActivity.key, productDataList.get(holder.getAdapterPosition()));
                context.startActivity(productPage);
            }
        });
        if (isCart) {
            if (removeEnabled)
                holder.removeCartIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cartItemClick.onRemoveCart(holder.getAdapterPosition());
                    }
                });
            else {

                holder.removeCartIV.setVisibility(View.GONE);
                holder.mainLL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
          setMargins(holder.mainLL,0,0,16,0);
            }
            holder.quantityTV.setText(productDataList.get(position).getCartQuantity() + "");
        }
    }
    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
    @Override
    public int getItemCount() {
        return productDataList.size();
    }

    public class ShopViewHolder extends RecyclerView.ViewHolder {
        ImageView productIV, removeCartIV;
        LinearLayout mainLL;
        TextView nameTV, priceTv, discountPrice, discountPercent, quantityTV;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            productIV = itemView.findViewById(R.id.productIV);
            nameTV = itemView.findViewById(R.id.productNameTV);
            mainLL = itemView.findViewById(R.id.mainLL);
            priceTv = itemView.findViewById(R.id.oldPriceTV);
            discountPrice = itemView.findViewById(R.id.discountPriceTV);
            if (isCart) {
                removeCartIV = itemView.findViewById(R.id.removeCartIV);
                quantityTV = itemView.findViewById(R.id.quantityTV);

            }

        }
    }

    public interface CartItemClick {
        public void onRemoveCart(int position);
    }
}
