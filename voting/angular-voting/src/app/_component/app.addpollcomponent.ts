import { Component } from "@angular/core";
import { Poll } from "../_model/app.poll";
import { PollService } from "../_service/app.pollservice";
import { Router } from "@angular/router";

@Component({
    selector:'addpoll',
    templateUrl:'../_html/app.addpoll.html'
})
export class AddPollComponent{
    poll:Poll={
        pollCenter:null,
        startTime:null,
        endTime:null
    }

    constructor(private service:PollService, private router:Router){}

    centerError=null;
    validateCenter():boolean{
        if(this.poll.pollCenter==null){
            this.centerError="Center cannot be empty!";
            return false;
        }
        else{
            this.centerError=null;
            return true;
        }
    }
    
    startTimeError=null;
    validateStartTime():boolean{
        if(this.poll.startTime==null){
            this.startTimeError="Start Time cannot be empty!";
            return false;
        }
        else{
            let currentDate = new Date().getTime();
            let startDate = new Date(this.poll.startTime).getTime();
            if(currentDate>startDate){
                this.startTimeError="Start Time cannot be in the past!";
                return false;
            }
            else if(startDate-currentDate>10*24*60*60*1000){
                this.startTimeError="Poll cannot be created more than 10 days before!";
                return false;
            }
            else{
                this.startTimeError=null;
                return true;
            }
        }
    }

    endTimeError=null;
    validateEndTime():boolean{
        if(this.poll.endTime==null){
            this.endTimeError="End Time cannot be empty!";
            return false;
        }
        else{
            let endDate = new Date(this.poll.endTime).getTime();
            let startDate = new Date(this.poll.startTime).getTime();
            if(endDate<startDate){
                this.endTimeError="End Time cannot be before Start Time!";
                return false;
            }
            else if(endDate-startDate > 10*60*60*1000){
                this.endTimeError="Poll cannot be created for more than 10 hours!";
                return false;
            }
            else{
                this.endTimeError=null;
                return true;
            }
        }
    }

    addPoll(){
        if(this.validateCenter() && this.validateStartTime() && this.validateEndTime()){
            this.service.addPoll(this.poll).subscribe((data)=>{alert("Poll added successfully!"),
                this.router.navigate(['/admin']).then(()=>window.location.reload())}, error=>{alert(error.error)});    
        }

    }
}