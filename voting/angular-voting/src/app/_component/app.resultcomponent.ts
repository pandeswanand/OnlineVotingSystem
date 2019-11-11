import { Component, OnInit } from "@angular/core";
import { UserService } from "../_service/app.userservice";
import { Poll } from "../_model/app.poll";

@Component({
    selector:'result',
    templateUrl:'../_html/app.result.html'
})
export class ResultComponent implements OnInit{
    constructor(private service:UserService){}

    poll:Poll={
        pollCenter:null,
        startTime:null,
        endTime:null
    }
    currentTime:any;

    ngOnInit(){
        this.service.searchUserPoll(sessionStorage.getItem("userEmail")).subscribe((data:Poll)=>
            {this.poll=data; this.getEndTime(), this.currentTime=new Date().getTime(); console.log(this.currentTime)})
    }

    getEndTime(){
        this.poll.endTime = new Date(this.poll.endTime).getTime();
        console.log(this.poll.endTime);
    }
}