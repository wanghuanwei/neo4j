/*
 * Copyright (c) 2002-2016 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.coreedge.raft.net;

import java.io.IOException;

import org.neo4j.coreedge.messaging.EndOfStreamException;
import org.neo4j.coreedge.identity.MemberId;
import org.neo4j.coreedge.core.state.machines.locks.ReplicatedLockTokenRequest;
import org.neo4j.storageengine.api.ReadableChannel;
import org.neo4j.storageengine.api.WritableChannel;

class ReplicatedLockTokenSerializer
{
    public static void marshal( ReplicatedLockTokenRequest tokenRequest, WritableChannel channel)
            throws IOException
    {
        channel.putInt( tokenRequest.id() );
        new MemberId.MemberIdMarshal().marshal( tokenRequest.owner(), channel );
    }

    public static ReplicatedLockTokenRequest unmarshal( ReadableChannel channel ) throws IOException, EndOfStreamException
    {
        int candidateId = channel.getInt();
        MemberId owner = new MemberId.MemberIdMarshal().unmarshal( channel );

        return new ReplicatedLockTokenRequest( owner, candidateId );
    }
}
