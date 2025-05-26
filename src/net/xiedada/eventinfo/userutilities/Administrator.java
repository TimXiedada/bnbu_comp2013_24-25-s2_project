/* SPDX-License-Identifier: Apache-2.0 */
/*
    Copyright (c) 2024 Xie Youtian. 

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package net.xiedada.eventinfo.userutilities;
import net.xiedada.eventinfo.exceptions.*;
import net.xiedada.eventinfo.eventutilities.*;

public class Administrator extends User{
    public Administrator(String username, String password) {
        super(username, password,User.UserType.ADMINISTRATOR);
    }
    public void suspendUser(User user) throws IllegalArgumentException, BadStatusException {
        // Logic to suspend or restore access for a user
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.locked) {
            user.locked = false;
            return; // Unlock the user if they are currently locked
        }
        if (user.isSignedIn()) {
            user.logout(); // Log out the user if they are currently signed in
        }
        user.locked = true; // Set the locked status to true
    }
    public void approveEvent(Event event) throws IllegalArgumentException, BadStatusException {
        // Logic to approve an event
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        if (event.approved()) {
            throw new BadStatusException("Event is already approved");
        }
        event.approve(); // Approve the event
    }
    public void disapproveEvent(Event event) throws IllegalArgumentException {
        // Logic to disapprove an event
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        event.disapprove(); // Disapprove the event
    }



}
