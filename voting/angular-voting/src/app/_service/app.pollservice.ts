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
        return this.http.post("http://localhost:9088/poll/add",form);
    }
}