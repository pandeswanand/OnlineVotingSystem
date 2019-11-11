import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";

@Injectable({
    providedIn:'root'
})
export class PollService{

    constructor(private http:HttpClient){}

    addPoll(poll:any){
        let form = new FormData();
        form.append("pollCenter", poll.pollCenter);
        form.append("startTime", poll.startTime);
        form.append("endTime", poll.endTime);
        return this.http.post("http://"+window.location.hostname+":9088/poll/add",form);
    }
}