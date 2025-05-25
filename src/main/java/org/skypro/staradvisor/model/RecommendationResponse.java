package org.skypro.staradvisor.model;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RecommendationResponse{
private UUID user_id;
private List<RecommendationDto> recommendations;

public RecommendationResponse(UUID user_id,List<RecommendationDto> recommendations){
    this.user_id=user_id;
    this.recommendations=recommendations;
}

public UUID getUser_id(){
    return user_id;
}

public void setUser_id(UUID user_id){
    this.user_id=user_id;
}

public List<RecommendationDto> getRecommendations(){
    return recommendations;
}

public void setRecommendations(List<RecommendationDto> recommendations){
    this.recommendations=recommendations;
}

@Override
public boolean equals(Object o){
    if(this==o)
        return true;
    if(o==null||getClass()!=o.getClass())
        return false;
    RecommendationResponse that=(RecommendationResponse)o;
    return Objects.equals(user_id,that.user_id)&&Objects.equals(recommendations,that.recommendations);
}

@Override
public int hashCode(){
    return Objects.hash(user_id,recommendations);
}

@Override
public String toString(){
    return "RecommendationResponse{"+"user_id="+user_id+", recommendations="+recommendations+'}';
}

}
